package com.emotion_apiserver.service;


import com.emotion_apiserver.config.util.EmotionContentMapper;
import com.emotion_apiserver.domain.emotion.EmotionType;
import com.emotion_apiserver.domain.content.RecommendCategoryType;
import com.emotion_apiserver.domain.dto.recommend.MovieInfo;
import com.emotion_apiserver.domain.dto.recommend.RecommendContentDto;
import com.emotion_apiserver.domain.dto.recommend.TVInfo;
import com.emotion_apiserver.domain.dto.recommend.TrackInfo;
import com.emotion_apiserver.repository.EmotionRecordRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RecommendationService {

    private final SpotifyService spotifyService;
    private final TmdbService tmdbService;
    private final EmotionRecordRepository emotionRecordRepository;

    public List<RecommendContentDto> generateRecommendationsByUser(Long userId) {
        EmotionType emotion = emotionRecordRepository
                .findTopByAccountIdOrderByCreatedAtDesc(userId)
                .orElseThrow(() -> new IllegalArgumentException("최근 감정 기록이 없습니다."))
                .getEmotion();

        return generateRecommendations(emotion);
    }

    public List<RecommendContentDto> generateRecommendations(EmotionType emotion) {
        String spotifyKeyword = EmotionContentMapper.getSpotifyKeyword(emotion);
        List<Integer> tmdbGenres = EmotionContentMapper.getTmdbGenres(emotion);

        TrackInfo trackInfo = spotifyService.searchTrackByKeyword(spotifyKeyword);
        MovieInfo movieInfo = tmdbService.searchMovieByGenres(tmdbGenres);
        TVInfo tvInfo = tmdbService.searchTVByGenres(tmdbGenres);

        List<RecommendContentDto> result = new ArrayList<>();

        if (trackInfo != null) {
            result.add(RecommendContentDto.builder()
                    .emotion(emotion)
                    .category(RecommendCategoryType.SONG)
                    .title(trackInfo.getTitle())
                    .link(trackInfo.getLink())
                    .thumbnail(trackInfo.getThumbnail())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        if (movieInfo != null) {
            result.add(RecommendContentDto.builder()
                    .emotion(emotion)
                    .category(RecommendCategoryType.MOVIE)
                    .title(movieInfo.getTitle())
                    .link(movieInfo.getLink())
                    .thumbnail(movieInfo.getThumbnail())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        if (tvInfo != null) {
            result.add(RecommendContentDto.builder()
                    .emotion(emotion)
                    .category(RecommendCategoryType.TV)
                    .title(tvInfo.getTitle())
                    .link(tvInfo.getLink())
                    .thumbnail(tvInfo.getThumbnail())
                    .createdAt(LocalDateTime.now())
                    .build());
        }

        return result;
    }
}