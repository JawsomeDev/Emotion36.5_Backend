package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.dto.emotion.EmotionRecordListDto;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;

import java.util.List;


public interface EmotionRecordRepositoryCustom {
    List<EmotionRecordListDto> getFiltered(Long id, PageRequestDto dto, String date, String emotion);
    int countFiltered(Long id, String date, String emotion);
}
