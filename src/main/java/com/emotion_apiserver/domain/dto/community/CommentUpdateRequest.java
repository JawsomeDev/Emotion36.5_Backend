package com.emotion_apiserver.domain.dto.community;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentUpdateRequest {

    @NotBlank
    private String content;
}
