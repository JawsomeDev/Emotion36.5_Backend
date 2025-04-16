package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.community.Community;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommunityRepository extends JpaRepository<Community, Long>, CommunityRepositoryCustom {


    @EntityGraph(attributePaths = {"author", "likedBy"})
    Optional<Community> findById(Long id);
}
