package com.emotion_apiserver.service;

import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.AccountRole;
import com.emotion_apiserver.domain.PasswordResetToken;
import com.emotion_apiserver.domain.dto.account.AccountSignUpRequest;
import com.emotion_apiserver.domain.dto.account.AccountSignUpResponse;
import com.emotion_apiserver.repository.AccountRepository;
import com.emotion_apiserver.repository.PasswordResetTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final PasswordResetTokenRepository resetTokenRepository;
    private final EmailService emailService;

    public AccountSignUpResponse signUp(AccountSignUpRequest request) {

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        if (accountRepository.getWithRoles(request.getEmail()) != null) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        if (accountRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("이미 사용 중인 닉네임입니다.");
        }

        Account account = Account.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .confirmPassword(passwordEncoder.encode(request.getConfirmPassword()))
                .nickname(request.getNickname())
                .createdAt(LocalDateTime.now())
                .accountRoleList(List.of(AccountRole.USER))
                .social(false)
                .build();

        Account saved = accountRepository.save(account);

        return new AccountSignUpResponse(saved.getId(), saved.getEmail(), saved.getNickname());
    }

    public void validateAndSendResetEmail(String email) {
        Account account = accountRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 이메일입니다."));

        // 토큰 생성
        String token = UUID.randomUUID().toString();

        // 임시 저장 (DB 저장 또는 Redis 등)
        resetTokenRepository.save(new PasswordResetToken(token, account));

        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        emailService.sendResetPasswordEmail(account.getNickname(), account.getEmail(), resetLink);
    }
}
