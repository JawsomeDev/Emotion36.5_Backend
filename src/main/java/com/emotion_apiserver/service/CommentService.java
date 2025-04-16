package com.emotion_apiserver.service;


import com.emotion_apiserver.domain.account.Account;
import com.emotion_apiserver.domain.community.Comment;
import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.dto.community.CommentCreateRequest;
import com.emotion_apiserver.domain.dto.community.CommentResponse;
import com.emotion_apiserver.repository.AccountRepository;
import com.emotion_apiserver.repository.CommentRepository;
import com.emotion_apiserver.repository.CommunityRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final AccountRepository accountRepository;
    private final CommunityRepository communityRepository;
    private final CommentRepository commentRepository;

    public Long createComment(CommentCreateRequest request, Long accountId) {
        Account author = getAccountOrThrow(accountId);
        Community community = getCommunityOrThrow(request.getCommunityId());

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .community(community)
                .likeCount(0)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);
        community.increaseCommentCount();
        return comment.getId();
    }


    public List<CommentResponse> getCommentsByCommunity(Long communityId) {
        Community community = getCommunityOrThrow(communityId);
        List<Comment> comments = commentRepository.findByCommunityOrderByCreatedAtDesc(community);
        return comments.stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
    }

    public long likeComment(Long commentId, Long accountId) {
        Comment comment = getCommentOrThrow(commentId);
        Account account = getAccountOrThrow(accountId);

        if (comment.getLikedBy().add(account)) {
            comment.changeLikeCount(comment.getLikeCount() + 1);
        }

        return comment.getLikeCount();
    }

    public long unlikeComment(Long commentId, Long accountId) {
        Comment comment = getCommentOrThrow(commentId);
        Account account = getAccountOrThrow(accountId);

        if (comment.getLikedBy().remove(account)) {
            comment.changeLikeCount(comment.getLikeCount() - 1);
        }

        return comment.getLikeCount();
    }

    public void updateComment(Long commentId, String content, Long accountId) {
        Comment comment = getCommentOrThrow(commentId);
        if (!comment.getAuthor().getId().equals(accountId)) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        comment.changeContent(content);
    }

    public void deleteComment(Long commentId, Long accountId) {
        Comment comment = getCommentOrThrow(commentId);

        if (!comment.getAuthor().getId().equals(accountId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        // 커뮤니티 조회 → 영속 상태 보장
        Community community = getCommunityOrThrow(comment.getCommunity().getId());
        community.decreaseCommentCount(); // 카운트 감소

        commentRepository.delete(comment);
    }

    private Account getAccountOrThrow(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다."));
    }

    private Community getCommunityOrThrow(Long id) {
        return communityRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
    }
    private Comment getCommentOrThrow(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 없습니다."));
    }

    public boolean isLikedByUser(Long commentId, Long accountId) {
        Comment comment = getCommentOrThrow(commentId);
        Account account = getAccountOrThrow(accountId);
        return comment.getLikedBy().contains(account);
    }
}
