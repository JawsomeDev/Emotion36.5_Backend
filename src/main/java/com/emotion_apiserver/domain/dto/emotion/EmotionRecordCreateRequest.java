package com.emotion_apiserver.domain.dto.emotion;

import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class EmotionRecordCreateRequest {

    @NotNull
    private EmotionType emotion; // 기본 감정

    @NotNull(message = "기록 날짜는 필수입니다.")
    @PastOrPresent(message = "미래 날짜는 선택할 수 없습니다.")
    private LocalDate recordDate; // 기록 날짜

    @NotBlank(message = "감정 일기는 필수입니다.")
    private String diary; // 감정 일기

    private boolean detailed; // 상세 모드 여부

    // 상세 모드 필드
    private String reason;
    private String situation;
    private String relatedPerson;
    private String reliefAttempt;
    private String reliefFailedReason;
    private String reliefSucceeded;
    private String prevention;

    private List<EmotionTag> emotionTags; // 감정 태그
}
