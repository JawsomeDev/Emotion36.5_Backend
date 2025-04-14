package com.emotion_apiserver.controller.community;


import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.service.AccountService;
import com.emotion_apiserver.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/communities")
public class CommunityLikeController {

    private final AccountService accountService;
    private final CommunityService communityService;


    // 좋아요 누르기
    @PostMapping("/{id}/like")
    public ResponseEntity<Long> like(@PathVariable Long id, @AuthenticationPrincipal AccountDto accountDto) {
        long likeCount = communityService.addLike(id, accountDto.getId());
        return ResponseEntity.ok(likeCount);
    }

    // 좋아요 여부 확인
    @GetMapping("/{id}/like")
    public ResponseEntity<?> isLiked(@PathVariable Long id, @AuthenticationPrincipal AccountDto accountDto) {
        if (accountDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        boolean liked = communityService.isLikedByUser(id, accountDto.getId());
        return ResponseEntity.ok(liked);
    }


    // 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Long> unlike(@PathVariable Long id, @AuthenticationPrincipal AccountDto accountDto) {
        long likeCount = communityService.removeLike(id, accountDto.getId());
        return ResponseEntity.ok(likeCount);
    }

}
