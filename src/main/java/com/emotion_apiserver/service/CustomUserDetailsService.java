package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Account account = accountRepository.getWithRoles(username);
        if(account == null) {
            throw new UsernameNotFoundException("Not Found");
        }
        return new AccountDto(
                account.getEmail(),
                account.getPassword(),
                account.isSocial(),
                account.getNickname(),
                account.getAccountRoleList().stream()
                        .map(Enum::name).collect(Collectors.toList()),
                account.getId());

    }
}
