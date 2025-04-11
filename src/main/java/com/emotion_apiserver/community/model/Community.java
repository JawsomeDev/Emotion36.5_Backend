package com.emotion_apiserver.community.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity  // JPA 엔티티 선언
public class Community {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가하는 기본 키 설정
    private Long id;

    private String title;  // 제목

    @Lob
    private String content;  // 내용 (길이를 늘리기 위해 @Lob 사용)

    private LocalDateTime createdDate;  // 생성일자
    private LocalDateTime updatedDate;  // 수정일자

    // 기본 생성자
    public Community() {}

    // 생성자
    public Community(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // Getter/Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    // 엔티티가 저장되기 전에 호출되는 메서드
    @PrePersist
    protected void onCreate() {
        createdDate = LocalDateTime.now();  // 생성일자 설정
    }

    // 엔티티가 업데이트될 때 호출되는 메서드
    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();  // 수정일자 설정
    }
}
