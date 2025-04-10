package com.emotion_apiserver.domain;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private LocalDateTime expiryDate;

    @OneToOne
    private Account account;

    public static PasswordResetToken create(String token, Account account) {
        return PasswordResetToken.builder()
                .token(token)
                .account(account)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    public PasswordResetToken(String token, Account account) {
        this.token = token;
        this.account = account;
        this.expiryDate = LocalDateTime.now().plusMinutes(30); // 유효 시간 30분 예시
    }
}
