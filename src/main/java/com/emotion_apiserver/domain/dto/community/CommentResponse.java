package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.community.Comment;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CommentResponse {

    private Long id;
    private String content;
    private Long authorId;
    private String authorNickname;
    private boolean isAuthor;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;
    private Long likeCount;
    private boolean isLiked;

    public CommentResponse(Comment comment, Account viewer) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorId = comment.getAuthor().getId();
        this.isAuthor = comment.getAuthor().getId().equals(viewer.getId());
        this.authorNickname = comment.getAuthor().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikeCount();
        this.isLiked = comment.getLikedBy().contains(viewer);
    }
}
