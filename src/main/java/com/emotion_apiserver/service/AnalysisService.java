package com.emotion_apiserver.service;

import com.emotion_apiserver.domain.analyse.AnalysisType;
import com.emotion_apiserver.domain.analyse.EmotionAnalysisSummary;
import com.emotion_apiserver.domain.emotion.EmotionRecord;
import com.emotion_apiserver.domain.emotion.EmotionType;
import com.emotion_apiserver.domain.dto.analysis.EmotionSummary;
import com.emotion_apiserver.domain.dto.analysis.EmotionTrendDto;
import com.emotion_apiserver.domain.dto.analysis.WeeklyEmotionAnalysisResponse;
import com.emotion_apiserver.repository.EmotionAnalysisSummaryRepository;
import com.emotion_apiserver.repository.EmotionRecordRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisService {

    private final EmotionRecordRepository emotionRecordRepository;
    private final EmotionAnalysisSummaryRepository summaryRepository;
    private final EmotionAIService emotionAIService;
    private final ObjectMapper objectMapper;

    public WeeklyEmotionAnalysisResponse getWeeklyAnalysis(Long accountId) {
        return loadOrGenerateAnalysis(accountId, AnalysisType.WEEKLY, 7);
    }

    public WeeklyEmotionAnalysisResponse getMonthlyAnalysis(Long accountId) {
        return loadOrGenerateAnalysis(accountId, AnalysisType.MONTHLY, 28);
    }

    public Map<String, Integer> getEmotionDistribution(Long accountId) {
        List<EmotionRecord> records = emotionRecordRepository.findByAccountId(accountId);

        return records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEmotion().getLabel(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
    }

    @Async
    public void regenerateAllAnalyses(Long accountId) {
        WeeklyEmotionAnalysisResponse weekly = buildAnalysisResponse(accountId, 7);
        WeeklyEmotionAnalysisResponse monthly = buildAnalysisResponse(accountId, 28);

        try {
            saveSummary(accountId, AnalysisType.WEEKLY, objectMapper.writeValueAsString(weekly));
            saveSummary(accountId, AnalysisType.MONTHLY, objectMapper.writeValueAsString(monthly));
        } catch (Exception e) {
            throw new RuntimeException("Failed to serialize analysis result.", e);
        }
    }

    private WeeklyEmotionAnalysisResponse loadOrGenerateAnalysis(Long accountId, AnalysisType type, int totalDays) {
        return summaryRepository.findByAccountIdAndType(accountId, type)
                .map(summary -> {
                    try {
                        return objectMapper.readValue(summary.getSummary(), WeeklyEmotionAnalysisResponse.class);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to deserialize analysis result.", e);
                    }
                })
                .orElseGet(() -> {
                    WeeklyEmotionAnalysisResponse response = buildAnalysisResponse(accountId, totalDays);
                    try {
                        saveSummary(accountId, type, objectMapper.writeValueAsString(response));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to serialize analysis result.", e);
                    }
                    return response;
                });
    }

    private void saveSummary(Long accountId, AnalysisType type, String json) {
        EmotionAnalysisSummary summary = summaryRepository.findByAccountIdAndType(accountId, type)
                .orElse(new EmotionAnalysisSummary());
        summary.setAccountId(accountId);
        summary.setType(type);
        summary.setSummary(json);
        summary.setUpdatedAt(LocalDateTime.now());
        summaryRepository.save(summary);
    }

    private WeeklyEmotionAnalysisResponse buildAnalysisResponse(Long accountId, int totalDays) {
        List<EmotionRecord> records = emotionRecordRepository.findByAccountId(accountId);
        records = records.stream()
                .filter(r -> r.getCreatedAt().isAfter(LocalDateTime.now().minusDays(totalDays)))
                .toList();

        Map<String, Integer> scoreMap = new LinkedHashMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd");

        if (records.isEmpty()) {
            return new WeeklyEmotionAnalysisResponse(List.of(), Map.of(), new EmotionSummary(List.of(), "데이터가 없습니다.", "최근 감정을 기록해보세요!"));
        }

        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(totalDays - 1);

        for (int i = 0; i < totalDays; i++) {
            LocalDate date = startDate.plusDays(i);
            scoreMap.put(date.format(formatter), 0);
        }

        Map<String, List<EmotionType>> byDate = records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getCreatedAt().toLocalDate().format(formatter),
                        Collectors.mapping(EmotionRecord::getEmotion, Collectors.toList())
                ));

        for (String date : scoreMap.keySet()) {
            List<EmotionType> emotions = byDate.getOrDefault(date, List.of());
            int sum = emotions.stream().mapToInt(this::mapEmotionToScore).sum();
            scoreMap.put(date, sum);
        }

        List<EmotionTrendDto> flow = scoreMap.entrySet().stream()
                .map(e -> new EmotionTrendDto(e.getKey(), e.getValue()))
                .collect(Collectors.toList());

        Map<String, Long> countByEmotion = records.stream()
                .collect(Collectors.groupingBy(
                        r -> r.getEmotion().getLabel(),
                        Collectors.counting()
                ));

        Map<String, Integer> emotionCount = countByEmotion.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().intValue()));

        List<Map.Entry<String, Integer>> sorted = new ArrayList<>(emotionCount.entrySet());
        sorted.sort((a, b) -> b.getValue() - a.getValue());

        List<String> topEmotions = sorted.stream()
                .limit(2)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        StringBuilder promptBuilder = new StringBuilder("최근 감정 기록 요약입니다.\n");
        emotionCount.forEach((key, value) -> promptBuilder.append("- ").append(key).append(": ").append(value).append("회\n"));
        promptBuilder.append("\n이 데이터를 기반으로 감정 흐름을 한 문장으로 요약하고, 사용자에게 적절한 감정 해소법이나 조언을 제시해주세요. ");
        promptBuilder.append("예를 들어, 사용자가 '화남'이라는 감정을 기록했다면, 화를 풀 수 있는 방법이나 평온을 찾을 수 있는 방법을 제안해 주세요. ");
        promptBuilder.append("감정을 진정시키기 위한 팁이나 위로가 될 수 있는 좋은 말도 포함시켜 주세요. 문장을 여러 개로 나누되 자연스럽게 이어주세요.");

        String result = emotionAIService.generateWeeklySummary(promptBuilder.toString());

        String pattern = "감정 흐름 요약을 불러오지 못했습니다.";
        String recommendation = "조금 더 자신을 돌보는 시간을 가져보세요.";

        if (result != null && !result.isBlank()) {
            String[] parts = result.split("\\. ", 2);
            if (parts.length >= 2) {
                pattern = parts[0].trim();
                recommendation = parts[1].trim();
            } else {
                pattern = result.trim();
            }
        }

        EmotionSummary summary = new EmotionSummary(topEmotions, pattern, recommendation);
        return new WeeklyEmotionAnalysisResponse(flow, emotionCount, summary);
    }

    private int mapEmotionToScore(EmotionType type) {
        return switch (type) {
            case JOY -> 2;
            case CALM -> 1;
            case TIRED -> -1;
            case SADNESS, ANGER, FEAR -> -2;
        };
    }
}


