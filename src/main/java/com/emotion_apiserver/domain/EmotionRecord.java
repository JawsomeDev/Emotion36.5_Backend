package com.emotion_apiserver.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmotionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    private Account account;

    // 기본 감정 (기쁨, 슬픔 등)
    @Enumerated(EnumType.STRING)
    private EmotionType emotion;

    // 기록 날짜 (생성일)
    private LocalDateTime createdAt;

    // 마지막 수정일
    private LocalDateTime modifiedAt;

    @Column(columnDefinition = "TEXT")
    private String diary;

    // [상세 모드] - 감정 분석 필드들
    @Column(columnDefinition = "TEXT")
    private String reason;             // 왜 그런 감정을 느꼈는가?
    @Column(columnDefinition = "TEXT")
    private String situation;         // 그 상황은 무엇인가?
    @Column(columnDefinition = "TEXT")
    private String relatedPerson;     // 누구 때문인가?
    @Column(columnDefinition = "TEXT")
    private String reliefAttempt;     // 해소 방법 시도
    @Column(columnDefinition = "TEXT")
    private String reliefFailedReason; // 왜 해소가 안 되었나?
    @Column(columnDefinition = "TEXT")
    private String reliefSucceeded;    // 해소가 된 방법
    @Column(columnDefinition = "TEXT")
    private String prevention;         // 방지 방법

    // 상세 모드 여부
    private boolean detailed;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private List<EmotionTag> emotionTags;

    // 수정 가능 여부 판단용
    public boolean isEditable() {
        return createdAt != null && createdAt.plusHours(24).isAfter(LocalDateTime.now());
    }
}
