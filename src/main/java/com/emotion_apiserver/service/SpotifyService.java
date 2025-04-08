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
    private long expiresAt = 0; // 만료 시간 (Unix timestamp)

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

        int expiresIn = json.getInt("expires_in"); // 보통 3600초
        this.expiresAt = System.currentTimeMillis() + (expiresIn * 1000L);
    }

    private void ensureTokenValid() {
        if (System.currentTimeMillis() >= expiresAt) {
            fetchAccessToken();
        }
    }

    public TrackInfo searchTrackByKeyword(String keyword) {
        ensureTokenValid(); // 💡 API 호출 전에 항상 유효성 검사

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

        return new TrackInfo(
                track.getString("name"),
                track.getJSONObject("external_urls").getString("spotify"),
                track.getJSONObject("album").getJSONArray("images").getJSONObject(0).getString("url")
        );
    }
}
