package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class CommunityListResponse {

    private Long id;
    private EmotionType emotion;
    private String content;
    private int likeCount;
    private int commentCount; // 추가됨
    private LocalDateTime createdAt;
    private String author; // 단순 문자열로 닉네임
    private boolean isLiked;

    public CommunityListResponse(CommunityListDto dto) {
        this.id = dto.getId();
        this.emotion = dto.getEmotion();
        this.content = dto.getContent();
        this.likeCount = dto.getLikeCount();
        this.commentCount = dto.getCommentCount(); // 새 필드 적용
        this.createdAt = dto.getCreatedAt();
        this.author = dto.getAuthorNickname(); // SimpleAccountResponse 대신 닉네임 문자열
        this.isLiked = dto.isLiked();
    }
}
