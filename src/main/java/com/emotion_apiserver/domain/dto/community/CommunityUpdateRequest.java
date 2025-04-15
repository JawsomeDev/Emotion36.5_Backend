package com.emotion_apiserver.domain.dto.community;


import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CommunityUpdateRequest {

    @NotBlank(message = "내용을 입력해주세요.")
    private String content;

    @NotNull(message = "감정을 선택해주세요.")
    private EmotionType emotion;



    private List<EmotionTag> emotionTags;
}
