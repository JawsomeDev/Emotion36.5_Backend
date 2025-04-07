package com.emotion_apiserver.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionAIService {

    private final ChatModel chatModel;

    public String generateWeeklySummary(String emotionSummaryText) {
        // 시스템 역할: 너는 감정 분석가야
        Message system = new SystemMessage("너는 감정 데이터를 분석해서 사람에게 요약과 조언을 제공하는 감정 분석 전문가야.");

        // 사용자 메시지: 감정 데이터 전달
        Message user = new UserMessage(emotionSummaryText);

        Prompt prompt = new Prompt(List.of(system, user));
        ChatResponse response = chatModel.call(prompt);

        return response.getResult().getOutput().getText();
    }
}
