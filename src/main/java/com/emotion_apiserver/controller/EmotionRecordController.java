package com.emotion_apiserver.controller;


import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordCreateRequest;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordListDto;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordUpdateRequest;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordUpdateResponse;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.emotion_apiserver.domain.dto.page.PageResponseDto;
import com.emotion_apiserver.service.EmotionRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/emotions")
@PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
@RequiredArgsConstructor
public class EmotionRecordController {

    private final EmotionRecordService emotionRecordService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody @Valid EmotionRecordCreateRequest request,
                                    @AuthenticationPrincipal AccountDto accountDto) {

        Account currentUser = Account.builder().id(accountDto.getId()).build();

        Long savedId = emotionRecordService.saveEmotionRecord(request, currentUser);
        return ResponseEntity.ok(savedId);
    }

    @GetMapping("/list")
    public ResponseEntity<?> getList(@AuthenticationPrincipal AccountDto accountDto,
                                     @ModelAttribute PageRequestDto pageRequestDto,
                                     @RequestParam(required = false) String date,
                                     @RequestParam(required = false) String emotion) {

        Long userId = accountDto.getId();

        PageResponseDto<EmotionRecordListDto> result =
                emotionRecordService.getEmotionRecords(userId, pageRequestDto, date, emotion);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @RequestBody @Valid EmotionRecordUpdateRequest request,
            @AuthenticationPrincipal AccountDto accountDto) {

        EmotionRecordUpdateResponse response = emotionRecordService.updateEmotionRecord(id, request, accountDto);
        return ResponseEntity.ok(response);
    }

    // 감정 기록 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal AccountDto accountDto) {

        emotionRecordService.deleteEmotionRecord(id, accountDto);
        return ResponseEntity.ok("삭제되었습니다.");
    }


}

