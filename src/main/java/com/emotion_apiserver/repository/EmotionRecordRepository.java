package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long>, EmotionRecordRepositoryCustom {
    List<EmotionRecord> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime from, LocalDateTime to);

    List<EmotionRecord> findByAccountId(Long accountId);
}
