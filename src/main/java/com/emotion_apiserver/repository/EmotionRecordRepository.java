package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.Account;
import com.emotion_apiserver.domain.EmotionRecord;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmotionRecordRepository extends JpaRepository<EmotionRecord, Long>, EmotionRecordRepositoryCustom {
    List<EmotionRecord> findByAccountIdAndCreatedAtBetween(Long accountId, LocalDateTime from, LocalDateTime to);

    List<EmotionRecord> findByAccountId(Long accountId);

    Optional<EmotionRecord> findTopByAccountIdOrderByCreatedAtDesc(Long accountId);
}
