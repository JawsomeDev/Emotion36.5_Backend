package com.emotion_apiserver.domain.dto.analysis;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
@AllArgsConstructor
public class WeeklyEmotionAnalysisResponse {

    private List<EmotionTrendDto> flow;              // 선그래프
    private Map<String, Integer> countByEmotion;     // 감정별 일수
    private EmotionSummary summary;
}
