package com.emotion_apiserver.domain.dto.account;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequest {

    @NotBlank(message = "인증 시간이 만료되어 재요청이 필요합니다.")
    private String token;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String newPassword;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String confirmPassword;
}
