package com.emotion_apiserver.domain.community;

import com.emotion_apiserver.domain.account.Account;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Account author;

    @ManyToOne(fetch = FetchType.LAZY)
    private Community community;

    private int commentCount;

    private long likeCount;

    @ManyToMany
    @JoinTable(
            name = "comment_likes",
            joinColumns = @JoinColumn(name = "comment_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    @Builder.Default
    private Set<Account> likedBy = new HashSet<>();

    public void changeLikeCount(long likeCount) {
        this.likeCount = likeCount;
    }

    public void changeContent(String content) {
        this.content = content;
    }


}