package com.emotion_apiserver.controller;


import com.emotion_apiserver.domain.EmotionType;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.recommend.RecommendContentDto;
import com.emotion_apiserver.service.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommend")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    @GetMapping("/latest")
    public ResponseEntity<List<RecommendContentDto>> recommend(@AuthenticationPrincipal AccountDto account) {
        return ResponseEntity.ok(recommendationService.generateRecommendationsByUser(account.getId()));
    }

}
