package com.emotion_apiserver.domain.dto.account;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Getter
@Setter
@ToString
public class AccountDto extends User {

    private String email;

    private String password;

    private String confirmPassword;

    private String nickname;

    private List<String> roleNames = new ArrayList<>();

    private boolean social;

    private LocalDateTime createdAt;

    public AccountDto(String email, String password, String confirmPassword,
                      boolean social, LocalDateTime createdAt, String nickname,
                      List<String> roleNames) {
        super(email, password, roleNames.stream().map(str ->
                new SimpleGrantedAuthority("ROLE_" + str)).collect(Collectors.toList()));
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.nickname = nickname;
        this.social = social;
        this.createdAt = createdAt;
        this.roleNames = roleNames;
    }

    public Map<String, Object> getClaims(){
        Map<String, Object> dataMap = new HashMap<>();
        dataMap.put("email", email);
        dataMap.put("password", password);
        dataMap.put("confirmPassword", confirmPassword);
        dataMap.put("nickname", nickname);
        dataMap.put("social", social);
        dataMap.put("createdAt", createdAt);
        dataMap.put("roleNames", roleNames);
        return dataMap;
    }
}
