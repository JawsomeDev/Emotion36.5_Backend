package com.emotion_apiserver.domain.dto.emotion;


import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionRecordListDto {

    private Long id;
    private String date;
    private EmotionType emotion;
    private List<EmotionTag> emotionTags;
    private String diary;

    private String reason;
    private String situation;
    private String relatedPerson;
    private String reliefAttempt;
    private String reliefFailedReason;
    private String reliefSucceeded;
    private String prevention;
}
