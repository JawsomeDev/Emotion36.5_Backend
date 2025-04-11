package com.emotion_apiserver.domain.dto.emotion;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class EmotionRecordUpdateRequest {

    @NotNull
    private String emotion;

    @NotBlank
    private String diary;

    private String reason;
    private String situation;
    private String relatedPerson;
    private String reliefAttempt;
    private String reliefFailedReason;
    private String reliefSucceeded;
    private String prevention;

    private List<String> emotionTags;
}
