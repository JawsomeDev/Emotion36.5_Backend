package com.emotion_apiserver.domain.dto.emotion;


import com.emotion_apiserver.domain.EmotionTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class EmotionRecordUpdateResponse {

    private Long id;
    private String emotion;
    private String diary;
    private String reason;
    private String situation;
    private String relatedPerson;
    private String reliefAttempt;
    private String reliefFailedReason;
    private String reliefSucceeded;
    private String prevention;
    private List<EmotionTag> emotionTags;
    private String modifiedAt;
}
