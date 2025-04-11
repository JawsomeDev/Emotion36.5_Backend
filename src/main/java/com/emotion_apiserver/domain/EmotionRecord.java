package com.emotion_apiserver.domain;

import com.emotion_apiserver.domain.dto.emotion.EmotionRecordUpdateRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

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

    // 기록 날짜
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
    @BatchSize(size = 50)
    @Enumerated(EnumType.STRING)
    @Cascade({org.hibernate.annotations.CascadeType.ALL, CascadeType.DELETE_ORPHAN})
    private List<EmotionTag> emotionTags = new ArrayList<>();

    // 수정 가능 여부 판단용
    public boolean isEditable() {
        return createdAt != null && createdAt.plusHours(24).isAfter(LocalDateTime.now());
    }

    public void updateFromRequest(EmotionRecordUpdateRequest request) {
        this.emotion = EmotionType.valueOf(request.getEmotion()); // 🔄 여기만 EmotionType으로
        this.diary = request.getDiary();
        this.reason = request.getReason();
        this.situation = request.getSituation();
        this.relatedPerson = request.getRelatedPerson();
        this.reliefAttempt = request.getReliefAttempt();
        this.reliefFailedReason = request.getReliefFailedReason();
        this.reliefSucceeded = request.getReliefSucceeded();
        this.prevention = request.getPrevention();

        this.emotionTags.clear();
        if (request.getEmotionTags() != null) {
            for (String tag : request.getEmotionTags()) {
                this.emotionTags.add(EmotionTag.valueOf(tag));
            }
        }
    }

}
