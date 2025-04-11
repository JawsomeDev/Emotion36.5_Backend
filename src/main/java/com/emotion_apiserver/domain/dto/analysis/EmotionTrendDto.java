package com.emotion_apiserver.domain.dto.analysis;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmotionTrendDto {
    private String data;

    private int score;
}
