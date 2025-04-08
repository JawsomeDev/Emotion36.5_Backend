package com.emotion_apiserver.domain.dto.recommend;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TrackInfo {

        private String title;
        private String link;
        private String thumbnail;

}
