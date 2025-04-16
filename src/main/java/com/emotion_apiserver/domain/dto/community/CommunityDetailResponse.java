package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class CommunityDetailResponse {

    private Long id;
    private String content;
    private EmotionType emotion;
    private List<EmotionTag> emotionTags;
    private Long likeCount;
    private boolean liked;
    private LocalDateTime createdAt;
    private SimpleAccountResponse author;
    private int commentCount;
    @JsonProperty("isAuthor")
    private boolean isAuthor;


    public boolean isAuthor() {
        return isAuthor;
    }

    public CommunityDetailResponse(Community community, Long viewerId) {
        this.id = community.getId();
        this.content = community.getContent();
        this.emotion = community.getEmotion();
        this.emotionTags = community.getEmotionTags();
        this.likeCount = community.getLikeCount();
        this.liked = community.getLikedBy().stream()
                .anyMatch(account -> account.getId().equals(viewerId));
        this.createdAt = community.getCreatedAt();
        this.author = new SimpleAccountResponse(community.getAuthor());
        this.commentCount = community.getCommentCount();
        this.isAuthor = community.getAuthor().getId().equals(viewerId);
    }
}
