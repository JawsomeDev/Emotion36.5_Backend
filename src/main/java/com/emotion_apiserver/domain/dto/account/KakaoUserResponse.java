package com.emotion_apiserver.domain.dto.account;


import lombok.Data;

@Data
public class KakaoUserResponse {

    private Long id;
    private Properties properties;

    @Data
    public static class Properties {
        private String nickname;
    }
}
