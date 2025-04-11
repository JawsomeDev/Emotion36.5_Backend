package com.emotion_apiserver.domain.dto.recommend;

import com.emotion_apiserver.domain.EmotionType;
import com.emotion_apiserver.domain.RecommendCategoryType;
import lombok.Builder;

import java.time.LocalDateTime;


@Builder
public record RecommendContentDto(
        EmotionType emotion,
        RecommendCategoryType category,
        String title,
        String link,
        String thumbnail,
        LocalDateTime createdAt) {

}
