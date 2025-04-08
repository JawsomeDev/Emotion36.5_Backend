package com.emotion_apiserver.service;

import com.emotion_apiserver.domain.dto.recommend.MovieInfo;
import com.emotion_apiserver.domain.dto.recommend.TVInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TmdbService {

    @Value("${tmdb.api-key}")
    private String apiKey;

    public MovieInfo searchMovieByGenres(List<Integer> genreIds) {
        RestTemplate restTemplate = new RestTemplate();

        String genresParam = genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String url = "https://api.themoviedb.org/3/discover/movie?api_key=" + apiKey +
                "&with_genres=" + genresParam + "&sort_by=popularity.desc&language=ko-KR";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());
        JSONArray results = json.getJSONArray("results");

        if (results.isEmpty()) return null;

        int randomIndex = new Random().nextInt(results.length());
        JSONObject result = results.getJSONObject(randomIndex);

        String title = result.getString("title");
        int id = result.getInt("id");
        String posterPath = result.optString("poster_path", null);
        String thumbnail = posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null;
        String link = "https://www.themoviedb.org/movie/" + id;

        log.info("🎬 추천 영화: {} / 링크: {}", title, link);

        return new MovieInfo(title, link, thumbnail);
    }

    public TVInfo searchTVByGenres(List<Integer> genreIds) {
        RestTemplate restTemplate = new RestTemplate();

        String genresParam = genreIds.stream()
                .map(String::valueOf)
                .collect(Collectors.joining(","));

        String url = "https://api.themoviedb.org/3/discover/tv?api_key=" + apiKey +
                "&with_genres=" + genresParam + "&with_origin_country=KR&sort_by=popularity.desc&language=ko-KR";

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONObject json = new JSONObject(response.getBody());
        JSONArray results = json.getJSONArray("results");

        if (results.isEmpty()) return null;

        int randomIndex = new Random().nextInt(results.length());
        JSONObject result = results.getJSONObject(randomIndex);

        String name = result.getString("name");
        int id = result.getInt("id");
        String posterPath = result.optString("poster_path", null);
        String thumbnail = posterPath != null ? "https://image.tmdb.org/t/p/w500" + posterPath : null;
        String link = "https://www.themoviedb.org/tv/" + id;

        log.info("📺 추천 드라마: {} / 링크: {}", name, link);

        return new TVInfo(name, link, thumbnail);
    }
}
