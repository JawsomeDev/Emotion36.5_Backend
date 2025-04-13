package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.analyse.AnalysisType;
import com.emotion_apiserver.domain.analyse.EmotionAnalysisSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmotionAnalysisSummaryRepository extends JpaRepository<EmotionAnalysisSummary, Long> {
    Optional<EmotionAnalysisSummary> findByAccountIdAndType(Long accountId, AnalysisType type);
}
