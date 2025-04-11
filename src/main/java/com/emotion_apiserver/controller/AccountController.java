package com.emotion_apiserver.controller;


import com.emotion_apiserver.domain.dto.account.AccountSignUpRequest;
import com.emotion_apiserver.domain.dto.account.AccountSignUpResponse;
import com.emotion_apiserver.repository.AccountRepository;
import com.emotion_apiserver.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountSignUpRequest request) {
        AccountSignUpResponse response = accountService.signUp(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response
        ));
    }
}
