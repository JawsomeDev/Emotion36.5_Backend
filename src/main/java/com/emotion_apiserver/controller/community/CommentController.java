package com.emotion_apiserver.controller.community;


import com.emotion_apiserver.domain.dto.account.AccountDto;
import com.emotion_apiserver.domain.dto.community.CommentCreateRequest;
import com.emotion_apiserver.domain.dto.community.CommentResponse;
import com.emotion_apiserver.domain.dto.community.CommentUpdateRequest;
import com.emotion_apiserver.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Long> createComment(@RequestBody @Valid CommentCreateRequest request,
                                              @AuthenticationPrincipal AccountDto accountDto) {
        Long id = commentService.createComment(request, accountDto.getId());
        return ResponseEntity.ok(id);
    }

    // 댓글 조회 (커뮤니티 ID 기준)
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getCommentsByCommunity(@RequestParam Long communityId) {
        List<CommentResponse> comments = commentService.getCommentsByCommunity(communityId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 좋아요
    @PostMapping("/{id}/like")
    public ResponseEntity<Long> likeComment(@PathVariable Long id,
                                            @AuthenticationPrincipal AccountDto accountDto) {
        long likeCount = commentService.likeComment(id, accountDto.getId());
        return ResponseEntity.ok(likeCount);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{id}/like")
    public ResponseEntity<Long> unlikeComment(@PathVariable Long id,
                                              @AuthenticationPrincipal AccountDto accountDto) {
        long likeCount = commentService.unlikeComment(id, accountDto.getId());
        return ResponseEntity.ok(likeCount);
    }

    // 댓글 수정
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateComment(@PathVariable Long id,
                                              @RequestBody @Valid CommentUpdateRequest request,
                                              @AuthenticationPrincipal AccountDto accountDto) {
        commentService.updateComment(id, request.getContent(), accountDto.getId());
        return ResponseEntity.ok().build();
    }

    // 댓글 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id, @AuthenticationPrincipal AccountDto accountDto) {
        commentService.deleteComment(id, accountDto.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/like")
    public ResponseEntity<?> isCommentLiked(@PathVariable Long id,
                                            @AuthenticationPrincipal AccountDto accountDto) {
        if (accountDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        boolean liked = commentService.isLikedByUser(id, accountDto.getId());
        return ResponseEntity.ok(liked);
    }

}
