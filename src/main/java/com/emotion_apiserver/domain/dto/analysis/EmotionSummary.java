package com.emotion_apiserver.domain.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class EmotionSummary {

    private List<String> topEmotions;
    private String pattern;
    private String recommendation;
}
