package com.emotion_apiserver.domain.analyse;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmotionAnalysisSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long accountId;

    @Enumerated(EnumType.STRING)
    private AnalysisType type;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String summary;

    private LocalDateTime updatedAt;
}
