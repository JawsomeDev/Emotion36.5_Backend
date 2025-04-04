package com.emotion_apiserver.domain.dto.account;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.bind.annotation.RestController;

@Getter
@AllArgsConstructor
public class AccountSignUpResponse {

    private Long id;
    private String email;
    private String nickname;
}
