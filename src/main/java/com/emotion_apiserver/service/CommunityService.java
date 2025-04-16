package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.community.*;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.emotion_apiserver.domain.dto.page.PageResponseDto;
import com.emotion_apiserver.domain.emotion.EmotionTag;
import com.emotion_apiserver.repository.AccountRepository;
import com.emotion_apiserver.repository.CommunityRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
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
            community.addLike(account);
        }

        return community.getLikeCount();
    }

    public long removeLike(Long communityId, Long accountId) {
        Community community = getCommunityOrThrow(communityId);
        Account account = getAccountOrThrow(accountId);

        if (community.getLikedBy().contains(account)) {
            community.removeLike(account);
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

        // ✅ 현재 로그인한 유저 ID 가져오기
        AccountDto accountDto = (AccountDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long currentUserId = accountDto.getId();

        // ✅ 유저 ID 넘기도록 repository 메서드 수정
        List<CommunityListDto> result = communityRepository.search(sort, emotionType, skip, size, currentUserId);

        int total = (emotionType == null)
                ? communityRepository.countAll()
                : communityRepository.countByEmotionType(emotionType);

        // DTO → 응답 객체로 변환
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

    public CommunityDetailResponse getCommunityDetail(Long id, Long viewerId) {
        Community community = communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));

        return new CommunityDetailResponse(community, viewerId);
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
