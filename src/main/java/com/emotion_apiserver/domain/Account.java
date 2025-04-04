package com.emotion_apiserver.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class Account {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickname;

    private String confirmPassword;

    private boolean social;

    @ElementCollection
    @Builder.Default
    @Enumerated(EnumType.STRING)
    private List<AccountRole> accountRoleList = new ArrayList<>();

    // 가입일시
    private LocalDateTime createdAt;
}
