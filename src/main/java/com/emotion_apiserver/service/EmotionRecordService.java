package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.EmotionRecord;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordCreateRequest;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordListDto;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordUpdateRequest;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.emotion_apiserver.domain.dto.page.PageResponseDto;
import com.emotion_apiserver.repository.EmotionRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionRecordService {

    private final EmotionRecordRepository emotionRecordRepository;

    public Long saveEmotionRecord(EmotionRecordCreateRequest dto, Account account) {
        EmotionRecord record = EmotionRecord.builder()
                .account(account)
                .emotion(dto.getEmotion())
                .createdAt(dto.getRecordDate().atStartOfDay())
                .modifiedAt(LocalDateTime.now())
                .diary(dto.getDiary())
                .detailed(dto.isDetailed())
                .reason(dto.getReason())
                .situation(dto.getSituation())
                .relatedPerson(dto.getRelatedPerson())
                .reliefAttempt(dto.getReliefAttempt())
                .reliefFailedReason(dto.getReliefFailedReason())
                .reliefSucceeded(dto.getReliefSucceeded())
                .prevention(dto.getPrevention())
                .emotionTags(dto.getEmotionTags())
                .build();

        EmotionRecord saved = emotionRecordRepository.save(record);
        return saved.getId();
    }

    public PageResponseDto<EmotionRecordListDto> getEmotionRecords(Long id, PageRequestDto dto, String date, String emotion) {

        int totalCount = emotionRecordRepository.countFiltered(id, date, emotion);

        List<EmotionRecordListDto> list = emotionRecordRepository.getFiltered(id, dto, date, emotion);

        return new PageResponseDto<>(dto, totalCount, list);
    }

    public void updateEmotionRecord(Long id, EmotionRecordUpdateRequest request, AccountDto accountDto) {
        EmotionRecord record = emotionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("감정 기록이 존재하지 않습니다."));

        if (!record.getAccount().getId().equals(accountDto.getId())) {
            throw new AccessDeniedException("수정 권한이 없습니다.");
        }

        record.updateFromRequest(request);
        // 태그는 기존 것 모두 삭제 후 다시 추가하는 식으로 처리할 수 있음
        emotionRecordRepository.save(record);
    }

    public void deleteEmotionRecord(Long id, AccountDto accountDto) {
        EmotionRecord record = emotionRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("감정 기록이 존재하지 않습니다."));

        if (!record.getAccount().getId().equals(accountDto.getId())) {
            throw new AccessDeniedException("삭제 권한이 없습니다.");
        }

        emotionRecordRepository.delete(record);
    }


}
