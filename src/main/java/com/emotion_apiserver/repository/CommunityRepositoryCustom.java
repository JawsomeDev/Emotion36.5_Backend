package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.dto.community.CommunityListDto;

import java.util.List;

public interface CommunityRepositoryCustom {

    List<CommunityListDto> search(String sort, String emotionType, int skip, int size, Long currentUserId);
    int countByEmotionType(String emotionType);
    int countAll();
}
