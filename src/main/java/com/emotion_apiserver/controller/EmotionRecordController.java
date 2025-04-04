package com.emotion_apiserver.controller;


import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordCreateRequest;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordListDto;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.emotion_apiserver.domain.dto.page.PageResponseDto;
import com.emotion_apiserver.service.EmotionRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotions")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
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

    @GetMapping("/list/{id}")
    public ResponseEntity<?> getList(
            @PathVariable Long id,
            @ModelAttribute PageRequestDto pageRequestDto,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String emotion) {

        PageResponseDto<EmotionRecordListDto> result =
                emotionRecordService.getEmotionRecords(id, pageRequestDto, date, emotion);

        return ResponseEntity.ok(result);
    }


}

