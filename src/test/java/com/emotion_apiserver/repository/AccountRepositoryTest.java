package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.account.AccountRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
public class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    public void saveDummyAccount() {
        Account account = Account.builder()
                .email("test@test.com")
                .password(passwordEncoder.encode("1111"))
                .confirmPassword(passwordEncoder.encode("1111"))
                .nickname("더미")
                .createdAt(LocalDateTime.now())
                .accountRoleList(List.of(AccountRole.USER))
                .build();

        Account saved = accountRepository.save(account);
        System.out.println("✅ 저장된 ID: " + saved.getId());
    }
}