package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.account.AccountRole;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.account.KakaoTokenResponse;
import com.emotion_apiserver.domain.dto.account.KakaoUserResponse;
import com.emotion_apiserver.repository.AccountRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class KakaoOAuthService {

    private final AccountRepository accountRepository;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();

    public AccountDto processLogin(String code) {
        // 1. 인가 코드로 access token 요청
        KakaoTokenResponse tokenResponse = getToken(code);

        // 2. access token으로 유저 정보 요청
        KakaoUserResponse userInfo = getUserInfo(tokenResponse.getAccess_token());
        String nickname = userInfo.getProperties().getNickname();

        // 3. 닉네임 기반 사용자 조회 또는 생성
        Account account = accountRepository.findByNickname(nickname)
                .orElseGet(() -> {
                    Account newAccount = Account.builder()
                            .nickname(nickname)
                            .password("KAKAO_LOGIN") // 의미 없는 비번
                            .social(true)
                            .accountRoleList(List.of(AccountRole.USER))
                            .build();
                    return accountRepository.save(newAccount);
                });
        List<String> roleNames = account.getAccountRoleList().stream()
                .map(Enum::name) // or role -> role.name()
                .toList();

        // 4. AccountDto 생성
        return new AccountDto(
                account.getEmail() != null ? account.getEmail() : nickname + "@kakao.temp",
                account.getPassword(),
                account.isSocial(),
                account.getNickname(),
                roleNames, // ✔️ String 리스트인 경우 그대로
                account.getId()
        );
    }

    private KakaoTokenResponse getToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                "https://kauth.kakao.com/oauth/token",
                request,
                KakaoTokenResponse.class
        );

        return response.getBody();
    }

    private KakaoUserResponse getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<KakaoUserResponse> response = restTemplate.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.GET,
                entity,
                KakaoUserResponse.class
        );

        return response.getBody();
    }
}
