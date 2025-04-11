package com.emotion_apiserver.domain;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public enum EmotionTag {
    행복, 설렘, 만족, 감사, 성취, 즐거움, // 기쁨 파트
    우울, 그리움, 상실, 외로움, 허무, 아쉬움, // 슬픔 파트
    분노, 짜증, 실망, 억울함, 답답함, 불만, // 화남 파트
    안정, 편안, 여유, 평화, 안심, // 평온 파트
    걱정, 긴장, 두려움, 초조, 스트레스, 혼란, // 불안 파트
    지침, 무기력, 나른함, 권태, 졸림, 에너지부족 // 피곤 파트
}
