package com.emotion_apiserver.domain;

import lombok.Getter;

@Getter
public enum EmotionType {

    JOY("기쁨", "😊"),
    SADNESS("슬픔", "😢"),
    ANGER("화남", "😠"),
    FEAR("불안", "😰"),
    CALM("평온", "😌"),
    TIRED("피곤", "😴");

    private final String label; // 한글 표현
    private final String emoji; // 아이콘용 이모지

    EmotionType(String label, String emoji) {
        this.label = label;
        this.emoji = emoji;
    }
}
