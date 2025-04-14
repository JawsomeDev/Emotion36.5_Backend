package com.emotion_apiserver.repository;

import com.emotion_apiserver.domain.community.Comment;
import com.emotion_apiserver.domain.community.Community;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByCommunityOrderByCreatedAtAsc(Community community);
}
