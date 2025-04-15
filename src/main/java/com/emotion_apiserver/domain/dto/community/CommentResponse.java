package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.community.Comment;
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
    private LocalDateTime createdAt;
    private Long likeCount;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.authorId = comment.getAuthor().getId();
        this.authorNickname = comment.getAuthor().getNickname();
        this.createdAt = comment.getCreatedAt();
        this.likeCount = comment.getLikeCount();
    }
}
