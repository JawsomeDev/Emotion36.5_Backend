package com.emotion_apiserver.domain.dto.account;


import lombok.Data;

@Data
public class KakaoTokenResponse {


    private String access_token;
    private String token_type;
    private String refresh_token;
    private Integer expires_in;
    private String scope;
}
