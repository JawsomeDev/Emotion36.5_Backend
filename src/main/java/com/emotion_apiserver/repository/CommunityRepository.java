package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityRepositoryCustom {
}
