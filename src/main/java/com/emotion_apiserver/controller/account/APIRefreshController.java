package com.emotion_apiserver.controller.account;


import com.emotion_apiserver.config.util.CustomJWTException;
import com.emotion_apiserver.config.util.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class APIRefreshController {


    @PostMapping("/api/member/refresh")
    public Map<String, Object> refresh(@RequestHeader("Authorization") String authHeader,
                                       @RequestBody Map<String, String> body) {

        String refreshToken = body.get("refreshToken");

        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new CustomJWTException("NULL_REFRESH_TOKEN");
        }

        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }

        String accessToken = authHeader.substring(7);

        // accessToken 만료 여부 확인
        if (!checkExpiredToken(accessToken)) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }

        // refreshToken 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);
        log.info("refresh ... claims: {}", claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) ?
                JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);
    }

    private boolean checkTime(Integer exp) {

        // JWT exp를 날짜로 변환
        Date expDate = new Date((long)exp * (1000));
        // 현재 시간과의 차이 계산
        long gap = expDate.getTime() - System.currentTimeMillis();
        //분 단위 계산
        long leftMin = gap / (1000 * 60);
        // 1시간도 안남았는지 확인
        return leftMin < 60;
    }

    private boolean checkExpiredToken(String token) {
        try{
            JWTUtil.validateToken(token);
        }catch(CustomJWTException ex){
            if(ex.getMessage().equals("Expired")){
                return true;
            }
        }
        return false;
    }


}
