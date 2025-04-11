package com.emotion_apiserver.controller;

import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.analysis.WeeklyEmotionAnalysisResponse;
import com.emotion_apiserver.service.AnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;


@RestController
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @GetMapping("/api/analysis/weekly")
    public ResponseEntity<WeeklyEmotionAnalysisResponse> getWeeklyAnalysis(@AuthenticationPrincipal AccountDto account) {
        return ResponseEntity.ok(analysisService.getWeeklyAnalysis(account.getId()));
    }

    @GetMapping("/api/analysis/monthly")
    public ResponseEntity<WeeklyEmotionAnalysisResponse> getMonthlyAnalysis(@AuthenticationPrincipal AccountDto account) {
        return ResponseEntity.ok(analysisService.getMonthlyAnalysis(account.getId()));
    }

    @GetMapping("/api/analysis/distribution")
    public ResponseEntity<Map<String, Integer>> getEmotionDistribution(@AuthenticationPrincipal AccountDto account) {
        return ResponseEntity.ok(analysisService.getEmotionDistribution(account.getId()));
    }
}
