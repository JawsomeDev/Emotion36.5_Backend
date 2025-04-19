package com.emotion_apiserver.domain.dto.account;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AccountSignUpRequest {

    @Email(message = "올바른 이메일 형식이어야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "올바른 이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일을 입력하세요.")
    private String email;

    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String password;

    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    @Size(min = 6, message = "비밀번호는 6자리 이상이어야 합니다.")
    private String confirmPassword;

    @NotBlank(message = "닉네임은 필수입니다.")
    @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-zA-Z0-9_-]{2,15}$", message = "2글자 이상 15글자 이하이며 _-를 제외한 특수기호는 사용이 불가합니다.")
    private String nickname;

    @NotNull(message = "이용약관에 동의해야 합니다.")
    private Boolean agreeToTerms;
}
