package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.EmotionRecord;
import com.emotion_apiserver.domain.dto.EmotionRecordCreateRequest;
import com.emotion_apiserver.domain.dto.EmotionRecordListDto;
import com.emotion_apiserver.domain.dto.PageRequestDto;
import com.emotion_apiserver.domain.dto.PageResponseDto;
import com.emotion_apiserver.repository.EmotionRecordRepository;
import com.emotion_apiserver.repository.EmotionRecordRepositoryCustom;
import lombok.RequiredArgsConstructor;
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
                .createdAt(dto.getRecordDate().atStartOfDay()) // 날짜만 받으므로 00시로 설정
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
}
