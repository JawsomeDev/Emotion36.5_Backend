package com.emotion_apiserver.domain.dto.community;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentCreateRequest {

    @NotNull
    private Long communityId;

    @NotBlank
    private String content;
}
