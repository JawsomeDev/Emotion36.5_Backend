package com.emotion_apiserver.repository;


import com.emotion_apiserver.domain.account.QAccount;
import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.community.QComment;
import com.emotion_apiserver.domain.community.QCommunity;
import com.emotion_apiserver.domain.dto.community.CommunityListDto;
import com.emotion_apiserver.domain.dto.community.QCommunityListDto;
import com.emotion_apiserver.domain.emotion.EmotionType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommunityRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<CommunityListDto> search(String sort, String emotionType, int skip, int size, Long currentUserId) {
        QCommunity c = QCommunity.community;
        QAccount a = QAccount.account;
        QAccount liked = new QAccount("liked");
        QCommunity likedCommunity = new QCommunity("likedCommunity");

        BooleanBuilder builder = new BooleanBuilder();
        if (emotionType != null) {
            builder.and(c.emotion.eq(EmotionType.valueOf(emotionType)));
        }

        OrderSpecifier<?> order = "likes".equals(sort)
                ? c.likeCount.desc()
                : c.createdAt.desc();

        return queryFactory
                .select(new QCommunityListDto(
                        c.id,
                        c.content,
                        c.emotion,
                        c.likeCount.intValue(),
                        c.commentCount.intValue(),
                        c.createdAt,
                        c.author.nickname,
                        JPAExpressions.selectOne()
                                .from(likedCommunity)
                                .join(likedCommunity.likedBy, liked)
                                .where(likedCommunity.id.eq(c.id)
                                        .and(liked.id.eq(currentUserId)))
                                .exists()
                ))
                .from(c)
                .join(c.author, a)
                .where(builder)
                .orderBy(order)
                .offset(skip)
                .limit(size)
                .fetch();
    }



    @Override
    public int countByEmotionType(String emotionType) {
        QCommunity community = QCommunity.community;

        return queryFactory
                .selectFrom(community)
                .where(community.emotion.eq(EmotionType.valueOf(emotionType)))
                .fetch().size();
    }

    @Override
    public int countAll() {
        QCommunity community = QCommunity.community;
        return (int) queryFactory.selectFrom(community).fetchCount();
    }
}
