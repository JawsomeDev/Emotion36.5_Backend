package com.emotion_apiserver.controller.community;


import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.community.CommunityCreateRequest;
import com.emotion_apiserver.domain.dto.community.CommunityDetailResponse;
import com.emotion_apiserver.domain.dto.community.CommunityListResponse;
import com.emotion_apiserver.domain.dto.community.CommunityUpdateRequest;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.emotion_apiserver.domain.dto.page.PageResponseDto;
import com.emotion_apiserver.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

    // 커뮤니티 글 작성
    @PostMapping
    public ResponseEntity<?> createCommunity(
            @RequestBody @Valid CommunityCreateRequest request,
            @AuthenticationPrincipal AccountDto accountDto) {

        Long id = communityService.createCommunity(request, accountDto.getId());
        return ResponseEntity.ok(Map.of(
                "id", id,
                "message", "게시글이 성공적으로 작성되었습니다."
        ));
    }

    // 커뮤니티 글 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<CommunityDetailResponse> getCommunity(@PathVariable Long id, @AuthenticationPrincipal AccountDto accountDto){
        CommunityDetailResponse dto = communityService.getCommunityDetail(id, accountDto.getId());
        return ResponseEntity.ok(dto);
    }

    // 커뮤니티 글 리스트 조회
    @GetMapping
    public ResponseEntity<PageResponseDto<CommunityListResponse>> getCommunityList(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String emotionType,
            @ModelAttribute PageRequestDto pageRequestDto){

        PageResponseDto<CommunityListResponse> response = communityService.getCommunityList(sort, emotionType, pageRequestDto);
        return ResponseEntity.ok(response);
    }

    // 커뮤니티 글 수정
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCommunity(@PathVariable Long id,
                                             @RequestBody @Valid CommunityUpdateRequest request,
                                             @AuthenticationPrincipal AccountDto accountDto) {
        communityService.updateCommunity(id, request, accountDto.getId());
        return ResponseEntity.ok().build();
    }

    // 게시글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommunity(@PathVariable Long id, @AuthenticationPrincipal AccountDto accountDto) {
        communityService.deleteCommunity(id, accountDto.getId());
        return ResponseEntity.noContent().build();
    }



}
