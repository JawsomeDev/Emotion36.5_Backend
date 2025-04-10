package com.emotion_apiserver.domain.dto.account;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {

    private String token;
    private String newPassword;
    private String confirmPassword;
}
