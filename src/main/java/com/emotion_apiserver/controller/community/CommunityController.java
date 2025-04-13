package com.emotion_apiserver.controller.community;


import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.community.CommunityCreateRequest;
import com.emotion_apiserver.service.CommunityService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityController {

    private final CommunityService communityService;

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
}
