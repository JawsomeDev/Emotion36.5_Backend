package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.community.Community;

import java.util.List;

public interface CommunityRepositoryCustom {

    List<Community> search(String sort, String emotionType, int skip, int size);
    int countByEmotionType(String emotionType);
    int countAll();
}
