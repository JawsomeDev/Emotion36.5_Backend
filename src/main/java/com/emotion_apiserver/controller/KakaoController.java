package com.emotion_apiserver.controller;


import com.emotion_apiserver.config.util.JWTUtil;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.service.KakaoOAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class KakaoController {

    private final KakaoOAuthService kakaoOAuthService;
    private final JWTUtil jwtUtil;

    /**
     * 카카오 소셜 로그인 처리
     * @param code 인가 코드
     */
    @GetMapping("/api/member/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam("code") String code) {
        log.info("🔐 카카오 로그인 요청 code = {}", code);

        // 1. 인가코드로 카카오 토큰 요청 + 사용자 정보 조회
        AccountDto dto = kakaoOAuthService.processLogin(code);

        // 2. JWT 토큰 생성
        String accessToken = jwtUtil.generateToken(dto.getClaims(), 10);       // 10분 유효
        String refreshToken = jwtUtil.generateToken(dto.getClaims(), 60 * 24); // 24시간 유효

        // 3. 응답 구성 (이메일 대신 nickname 기반)
        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken,
                "id", dto.getId(),
                "nickname", dto.getNickname(),
                "isSocial", dto.isSocial()
        ));
    }
}
