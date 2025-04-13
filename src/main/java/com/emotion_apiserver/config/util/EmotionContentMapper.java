package com.emotion_apiserver.config.util;
import com.emotion_apiserver.domain.emotion.EmotionType;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class EmotionContentMapper {

    private static final Map<EmotionType, String> spotifyKeywordMap = Map.of(
            EmotionType.JOY, "happy",
            EmotionType.SADNESS, "sad",
            EmotionType.ANGER, "angry",
            EmotionType.CALM, "chill",
            EmotionType.TIRED, "sleep",
            EmotionType.FEAR, "dark"
    );

    private static final Map<EmotionType, List<Integer>> tmdbGenreMap = Map.of(
            EmotionType.JOY, List.of(35), // Comedy
            EmotionType.SADNESS, List.of(18), // Drama
            EmotionType.ANGER, List.of(28), // Action
            EmotionType.FEAR, List.of(27), // Horror
            EmotionType.CALM, List.of(10749, 14), // Romance, Fantasy
            EmotionType.TIRED, List.of(10402) // Music
    );

    public static String getSpotifyKeyword(EmotionType emotion) {
        return spotifyKeywordMap.getOrDefault(emotion, "chill");
    }

    public static List<Integer> getTmdbGenres(EmotionType emotion) {
        return tmdbGenreMap.getOrDefault(emotion, List.of(18)); // 기본 Drama
    }
}


