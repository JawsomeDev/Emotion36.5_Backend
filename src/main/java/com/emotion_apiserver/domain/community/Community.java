package com.emotion_apiserver.domain.community;

import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.domain.emotion.EmotionType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Community {
    @Id @GeneratedValue
    private Long id;

    private String title;

    @Lob
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    @Enumerated(EnumType.STRING)
    private EmotionType emotion;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    private List<EmotionTag> emotionTags = new ArrayList<>();

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    private long likeCount;

    @ManyToMany
    @JoinTable(
            name = "community_likes",
            joinColumns = @JoinColumn(name = "community_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private Set<Account> likedBy = new HashSet<>();

    public void addLike(Account account) {
        if (this.likedBy.add(account)) {
            this.likeCount++;
        }
    }

    public void removeLike(Account account) {
        if (this.likedBy.remove(account)) {
            this.likeCount--;
        }
    }

    public void update(String title, String content, EmotionType emotion, List<EmotionTag> emotionTags) {
        this.title = title;
        this.content = content;
        this.emotion = emotion;
        this.emotionTags = emotionTags;
        this.updatedAt = LocalDateTime.now(); // 수정일 갱신
    }
}