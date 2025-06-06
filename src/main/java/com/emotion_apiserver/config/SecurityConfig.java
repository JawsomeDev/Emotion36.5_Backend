package com.emotion_apiserver.config;


import com.emotion_apiserver.config.filter.JWTCheckFilter;
import com.emotion_apiserver.config.handler.APILoginFailHandler;
import com.emotion_apiserver.config.handler.APILoginSuccessHandler;
import com.emotion_apiserver.config.handler.CustomAccessDeniedHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth.requestMatchers("/", "/api/member/**",  "/login", "/api/password/**",
                                        "/v3/api-docs/**",
                                        "/v3/api-docs",
                                        "/swagger-ui/**",
                                        "/swagger-ui.html",
                                        "/swagger-resources/**",      // ← 요거 추가 필요할 수 있음
                                        "/webjars/**"
                                ).permitAll()
                                .anyRequest().authenticated());

        http
                .formLogin( formLogin -> {
                        formLogin.loginPage("/api/member/login");
                        formLogin.successHandler(new APILoginSuccessHandler());
                        formLogin.failureHandler(new APILoginFailHandler());
                });

        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()));

        http
                .csrf(AbstractHttpConfigurer::disable);


        http
                .addFilterBefore(new JWTCheckFilter(),
                        UsernamePasswordAuthenticationFilter.class); // JWT 체크

        http
                .exceptionHandling(exception -> {
                    exception.accessDeniedHandler(new CustomAccessDeniedHandler());
                });

        return http.build();
    }


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
