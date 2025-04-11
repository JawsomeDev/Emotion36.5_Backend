package com.emotion_apiserver.community.service;

import com.emotion_apiserver.community.model.Community;
import com.emotion_apiserver.community.repository.CommunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommunityService {

    private final CommunityRepository communityRepository;

    @Autowired
    public CommunityService(CommunityRepository communityRepository) {
        this.communityRepository = communityRepository;
    }

    // 게시물 리스트
    public List<Community> findAll() {
        return communityRepository.findAll();
    }

    // 게시물 ID로 조회
    public Optional<Community> findById(Long id) {
        return communityRepository.findById(id);
    }

    // 새 게시물 저장
    public Community save(Community community) {
        return communityRepository.save(community);
    }

    // 게시물 삭제
    public void delete(Long id) {
        communityRepository.deleteById(id);
    }
}
