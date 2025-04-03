package com.emotion_apiserver.repository;


import com.emotion_apiserver.domain.EmotionRecord;
import com.emotion_apiserver.domain.EmotionTag;
import com.emotion_apiserver.domain.EmotionType;
import com.emotion_apiserver.domain.QEmotionRecord;
import com.emotion_apiserver.domain.dto.emotion.EmotionRecordListDto;
import com.emotion_apiserver.domain.dto.page.PageRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class EmotionRecordRepositoryImpl implements EmotionRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public EmotionRecordRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public List<EmotionRecordListDto> getFiltered(Long id, PageRequestDto dto, String date, String emotion) {
        QEmotionRecord record = QEmotionRecord.emotionRecord;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(record.account.id.eq(id));

        if (date != null) builder.and(record.createdAt.eq(LocalDate.parse(date).atStartOfDay()));
        if (emotion != null && !emotion.equals("all")) builder.and(record.emotion.eq(EmotionType.valueOf(emotion)));

        List<EmotionRecordListDto> dtos = queryFactory
                .select(Projections.fields(EmotionRecordListDto.class,
                        record.id,
                        Expressions.stringTemplate("DATE_FORMAT({0}, '%Y-%m-%d')", record.createdAt).as("date"),
                        record.emotion,
                        record.diary,
                        record.reason,
                        record.situation,
                        record.relatedPerson,
                        record.reliefAttempt,
                        record.reliefFailedReason,
                        record.reliefSucceeded,
                        record.prevention
                ))
                .from(record)
                .where(builder)
                .orderBy(record.createdAt.desc())
                .offset(dto.getSkip())
                .limit(dto.getSize())
                .fetch();

        List<Long> ids = dtos.stream().map(EmotionRecordListDto::getId).collect(Collectors.toList());
        List<EmotionRecord> records = queryFactory
                .selectFrom(record)
                .where(record.id.in(ids))
                .fetch();

        Map<Long, List<EmotionTag>> tagMap = records.stream()
                .collect(Collectors.toMap(
                        EmotionRecord::getId,
                        EmotionRecord::getEmotionTags
                ));

        for (EmotionRecordListDto dtoItem : dtos) {
            dtoItem.setEmotionTags(tagMap.getOrDefault(dtoItem.getId(), List.of()));
        }

        return dtos;
    }

    @Override
    public int countFiltered(Long id, String date, String emotion) {
        QEmotionRecord record = QEmotionRecord.emotionRecord;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(record.account.id.eq(id));
        if (date != null) builder.and(record.createdAt.eq(LocalDate.parse(date).atStartOfDay()));
        if (emotion != null && !emotion.equals("all")) builder.and(record.emotion.stringValue().eq(emotion));

        Long count = queryFactory
                .select(record.count())
                .from(record)
                .where(builder)
                .fetchOne();

        return count != null ? count.intValue() : 0;
    }
}
