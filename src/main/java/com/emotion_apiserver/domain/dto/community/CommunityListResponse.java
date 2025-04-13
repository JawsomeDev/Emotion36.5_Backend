package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommunityListResponse {

    private Long id;
    private String title;
    private EmotionType emotion;
    private List<EmotionTag> emotionTags;
    private Long likeCount;
    private LocalDateTime createdAt;

    public CommunityListResponse(Community c) {
        this.id = c.getId();
        this.title = c.getTitle();
        this.emotion = c.getEmotion();
        this.emotionTags = c.getEmotionTags();
        this.likeCount = c.getLikeCount();
        this.createdAt = c.getCreatedAt();
    }
}
