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
    private EmotionType emotion;
    private List<EmotionTag> emotionTags;
    private String content;
    private Long likeCount;
    private LocalDateTime createdAt;
    private SimpleAccountResponse author;
    private int commentCount;

    public CommunityListResponse(Community c) {
        this.content = c.getContent();
        this.id = c.getId();
        this.emotion = c.getEmotion();
        this.emotionTags = c.getEmotionTags();
        this.likeCount = c.getLikeCount();
        this.createdAt = c.getCreatedAt();
        this.author = new SimpleAccountResponse(c.getAuthor());
        this.commentCount = c.getComments().size(); // 이걸로 끝
    }
}
