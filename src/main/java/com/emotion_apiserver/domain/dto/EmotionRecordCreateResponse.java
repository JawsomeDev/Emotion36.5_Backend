package com.emotion_apiserver.domain.dto;

import com.emotion_apiserver.domain.EmotionRecord;
import com.emotion_apiserver.domain.EmotionTag;
import com.emotion_apiserver.domain.EmotionType;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionRecordCreateResponse {

    private Long id;

    private EmotionType emotion; // 기본 감정

    private LocalDate recordDate; // 기록 날짜

    private String diary; // 감정 일기

    private boolean detailed; // 상세 작성 여부

    // 상세 작성 모드 필드들
    private String reason;
    private String situation;
    private String relatedPerson;
    private String reliefAttempt;
    private String reliefFailedReason;
    private String reliefSucceeded;
    private String prevention;

    private List<EmotionTag> emotionTags; // 감정 태그들

    private boolean editable; // 수정 가능 여부 (24시간 내)


    //Entity -> DTO
    public static EmotionRecordCreateResponse fromEntity(EmotionRecord record) {
        return EmotionRecordCreateResponse.builder()
                .id(record.getId())
                .emotion(record.getEmotion())
                .recordDate(record.getCreatedAt().toLocalDate())
                .diary(record.getDiary())
                .detailed(record.isDetailed())
                .reason(record.getReason())
                .situation(record.getSituation())
                .relatedPerson(record.getRelatedPerson())
                .reliefAttempt(record.getReliefAttempt())
                .reliefFailedReason(record.getReliefFailedReason())
                .reliefSucceeded(record.getReliefSucceeded())
                .prevention(record.getPrevention())
                .emotionTags(record.getEmotionTags())
                .editable(record.isEditable())
                .build();
    }
}