package com.emotion_apiserver.config.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyStore;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

@Component
public class JWTUtil {


    private static String key;

    @Value("${jwt.secret}")
    public void setKey(String key) {
        JWTUtil.key = key;
    }

    public static String generateToken(Map<String, Object> valueMap, int min){
        SecretKey key = null;

        try{
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes(StandardCharsets.UTF_8));
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }

        return Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
    }

    public static Map<String, Object> validateToken(String token){
        Map<String, Object> claim = null;
        try{
            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes(StandardCharsets.UTF_8));
            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        }catch (MalformedJwtException malformedJwtException){
            throw new CustomJWTException("MalFormed");
        }catch (ExpiredJwtException expiredJwtException){
            throw new CustomJWTException("Expired");
        }catch (InvalidClaimException invalidClaimException){
            throw new CustomJWTException("Invalid");
        }catch(JwtException jwtException){
            throw new CustomJWTException("JWTError");
        }catch (Exception e){
            throw new CustomJWTException("Error");
        }
        return claim;
    }
}
