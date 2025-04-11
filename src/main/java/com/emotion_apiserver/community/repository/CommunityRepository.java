package com.emotion_apiserver.community.repository;

import com.emotion_apiserver.community.model.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long> {
    // 기본 CRUD 메서드들이 제공됨
}
