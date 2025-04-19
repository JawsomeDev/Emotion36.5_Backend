package com.emotion_apiserver.domain.dto.community;

import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;


@Data
public class CommunityListDto {
    private Long id;
    private String content;
    private EmotionType emotion;
    private int likeCount;
    private int commentCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private String authorNickname;
    private boolean liked;

    @QueryProjection
    public CommunityListDto(Long id, String content, EmotionType emotion, int likeCount, int commentCount, LocalDateTime createdAt, String authorNickname, boolean liked) {
        this.id = id;
        this.content = content;
        this.emotion = emotion;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.createdAt = createdAt;
        this.authorNickname = authorNickname;
        this.liked = liked;
    }
}


