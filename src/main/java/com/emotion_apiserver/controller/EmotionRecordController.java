package com.emotion_apiserver.controller;


import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.dto.EmotionRecordCreateRequest;
import com.emotion_apiserver.service.EmotionRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/emotions")
@RequiredArgsConstructor
public class EmotionRecordController {

    private final EmotionRecordService emotionRecordService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid EmotionRecordCreateRequest request) {

        // [임시] 현재 인증된 사용자. 실제로는 Spring Security에서 가져올 예정
        Account dummyUser = Account.builder().id(1L).build();

        Long savedId = emotionRecordService.saveEmotionRecord(request, dummyUser);
        return ResponseEntity.ok().body(savedId);
    }
}

