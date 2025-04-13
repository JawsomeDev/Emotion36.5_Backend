package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.emotion.EmotionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CommunityCreateRequest {

    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "감정을 선택해주세요.")
    private EmotionType emotion;

    private List<String> emotionTags;
}
