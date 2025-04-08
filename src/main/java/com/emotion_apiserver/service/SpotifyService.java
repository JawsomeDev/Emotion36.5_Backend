package com.emotion_apiserver.service;

import com.emotion_apiserver.domain.dto.recommend.TrackInfo;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpotifyService {

    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    private String accessToken;

    @PostConstruct
    public void init() {
        fetchAccessToken();
    }

    private void fetchAccessToken() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(clientId, clientSecret);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://accounts.spotify.com/api/token",
                request,
                String.class
        );

        JSONObject json = new JSONObject(response.getBody());
        this.accessToken = json.getString("access_token");
    }

    public TrackInfo searchTrackByKeyword(String keyword) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.spotify.com/v1/search?q=" + URLEncoder.encode(keyword, StandardCharsets.UTF_8)
                + "&type=track&limit=10&market=KR";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        JSONObject json = new JSONObject(response.getBody());
        JSONArray items = json.getJSONObject("tracks").getJSONArray("items");

        if (items.isEmpty()) return null;

        int randomIndex = new Random().nextInt(items.length());
        JSONObject track = items.getJSONObject(randomIndex);
        String trackName = track.getString("name");
        String link = track.getJSONObject("external_urls").getString("spotify");
        String thumbnail = track.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url");

        log.info("🎵 추천된 트랙: {} / 링크: {}", trackName, link);
        return new TrackInfo(trackName, link, thumbnail);
    }
}