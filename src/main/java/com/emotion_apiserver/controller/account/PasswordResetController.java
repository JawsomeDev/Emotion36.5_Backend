package com.emotion_apiserver.controller.account;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.account.PasswordResetToken;
import com.emotion_apiserver.domain.dto.account.PasswordResetRequest;
import com.emotion_apiserver.repository.AccountRepository;
import com.emotion_apiserver.repository.PasswordResetTokenRepository;
import com.emotion_apiserver.service.AccountService;
import com.emotion_apiserver.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/password")
@RequiredArgsConstructor
public class PasswordResetController {

    private final EmailService emailService;
    private final AccountService accountService;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/request-reset")
    public ResponseEntity<?> requestReset(@RequestParam String email) {
        accountService.validateAndSendResetEmail(email); // 존재 여부 확인 및 메일 전송
        return ResponseEntity.ok("비밀번호 재설정 링크가 이메일로 전송되었습니다.");
    }

    @GetMapping("/reset")
    public ResponseEntity<?> validateToken(@RequestParam("token") String token) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 토큰입니다."));

        if (resetToken.isExpired()) {
            return ResponseEntity.badRequest().body("토큰이 만료되었습니다.");
        }

        // React 페이지로 리다이렉트하거나 상태 전달
        URI redirect = URI.create("http://localhost:5173/reset-password?token=" + token);
        return ResponseEntity.status(HttpStatus.FOUND).location(redirect).build();
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetRequest request) {
        PasswordResetToken token = resetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new IllegalArgumentException("토큰이 유효하지 않습니다."));

        if (token.isExpired()) {
            throw new IllegalArgumentException("토큰이 만료되었습니다. 재요청이 필요합니다.");
        }
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        Account account = token.getAccount();
        account.changePassword(passwordEncoder.encode(request.getNewPassword()));
        accountRepository.save(account);

        resetTokenRepository.delete(token);


        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "비밀번호가 성공적으로 변경되었습니다."
                )
        );

    }
}

