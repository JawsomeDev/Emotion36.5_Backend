package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.dto.community.CommunityCreateRequest;
import com.emotion_apiserver.domain.dto.community.CommunityDetailResponse;
import com.emotion_apiserver.domain.dto.community.CommunityListResponse;
import com.emotion_apiserver.domain.dto.community.CommunityUpdateRequest;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.emotion_apiserver.domain.dto.page.PageResponseDto;
import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.repository.AccountRepository;
import com.emotion_apiserver.repository.CommunityRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommunityService {

    private final CommunityRepository communityRepository;
    private final AccountRepository accountRepository;

    public long addLike(Long communityId, Long accountId) {
        Community community = getCommunityOrThrow(communityId);
        Account account = getAccountOrThrow(accountId);

        if (!community.getLikedBy().contains(account)) {
            community.addLike(account); // 의미 있는 도메인 메서드 호출
        }

        return community.getLikeCount();
    }

    public long removeLike(Long communityId, Long accountId) {
        Community community = getCommunityOrThrow(communityId);
        Account account = getAccountOrThrow(accountId);

        if (community.getLikedBy().contains(account)) {
            community.removeLike(account); // 의미 있는 도메인 메서드 호출
        }

        return community.getLikeCount();
    }

    public boolean isLikedByUser(Long communityId, Long accountId) {
        Community community = getCommunityOrThrow(communityId);
        Account account = getAccountOrThrow(accountId);
        return community.getLikedBy().contains(account);
    }

    private Community getCommunityOrThrow(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
    }

    private Account getAccountOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
    }

    public PageResponseDto<CommunityListResponse> getCommunityList(String sort, String emotionType, PageRequestDto pageRequestDto) {
        int skip = pageRequestDto.getSkip();
        int size = pageRequestDto.getSize();

        List<Community> result = communityRepository.search(sort, emotionType, skip, size);
        int total = (emotionType == null) ? communityRepository.countAll() : communityRepository.countByEmotionType(emotionType);

        List<CommunityListResponse> content = result.stream()
                .map(CommunityListResponse::new)
                .collect(Collectors.toList());

        return new PageResponseDto<>(pageRequestDto, total, content);
    }


    public Long createCommunity(CommunityCreateRequest request, Long accountId) {
        Account author = getAccountOrThrow(accountId);

        Community community = Community.builder()
                .content(request.getContent())
                .author(author)
                .emotion(request.getEmotion())
                .emotionTags(convertTags(request.getEmotionTags()))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeCount(0)
                .build();

        communityRepository.save(community);
        return community.getId();
    }

    private List<EmotionTag> convertTags(List<String> tags) {
        return tags == null ? List.of() :
                tags.stream().map(EmotionTag::valueOf).collect(Collectors.toList());
    }

    public CommunityDetailResponse getCommunityDetail(Long id, Long accountId) {
        Community community = getCommunityOrThrow(id);
        Account account = getAccountOrThrow(accountId);
        return new CommunityDetailResponse(community, account);
    }

    public void updateCommunity(Long id, @Valid CommunityUpdateRequest request, Long accountId) {
        Community community = getCommunityOrThrow(id);
        Account account = getAccountOrThrow(accountId);

        if(!community.getAuthor().getId().equals(account.getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        community.update(request.getContent(), request.getEmotion(), request.getEmotionTags());
    }

    public void deleteCommunity(Long id, Long accountId) {
        Community community = getCommunityOrThrow(id);
        Account account = getAccountOrThrow(accountId);

        if (!community.getAuthor().getId().equals(account.getId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        communityRepository.delete(community);
    }
}
