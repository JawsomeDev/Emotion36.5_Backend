package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.emotion.EmotionRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long>, EmotionRecordRepositoryCustom {
    List<EmotionRecord> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime from, LocalDateTime to);

    List<EmotionRecord> findByAccountId(Long accountId);

    Optional<EmotionRecord> findTopByAccountIdOrderByCreatedAtDesc(Long accountId);
}
