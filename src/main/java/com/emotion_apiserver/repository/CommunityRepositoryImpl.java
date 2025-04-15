package com.emotion_apiserver.repository;


import com.emotion_apiserver.domain.community.Community;
import com.emotion_apiserver.domain.community.QCommunity;
import com.emotion_apiserver.domain.emotion.EmotionType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class CommunityRepositoryImpl implements CommunityRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public CommunityRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<Community> search(String sort, String emotionType, int skip, int size) {
        QCommunity community = QCommunity.community;

        BooleanBuilder builder = new BooleanBuilder();

        if (emotionType != null) {
            builder.and(community.emotion.eq(EmotionType.valueOf(emotionType)));
        }

        OrderSpecifier<?> order = sort != null && sort.equals("likes")
                ? community.likeCount.desc()
                : community.createdAt.desc();

        return queryFactory
                .selectFrom(community)
                .leftJoin(community.comments).fetchJoin()
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
