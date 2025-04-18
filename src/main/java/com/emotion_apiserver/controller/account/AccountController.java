package com.emotion_apiserver.controller.account;


import com.emotion_apiserver.domain.dto.account.AccountSignUpRequest;
import com.emotion_apiserver.domain.dto.account.AccountSignUpResponse;
import com.emotion_apiserver.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class AccountController {

    private final AccountService accountService;

    @GetMapping("/health")
    public String healthCheck() {
        return "SUCCESS HEALTH CHECK";
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody AccountSignUpRequest request) {
        AccountSignUpResponse response = accountService.signUp(request);
        return ResponseEntity.ok(Map.of(
                "success", true,
                "data", response
        ));
    }
}
