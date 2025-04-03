package com.emotion_apiserver.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class PageRequestDto {


    private int page = 1;

    private int size = 10;


    public int getSkip() {
        return (page - 1) * size;
    }

}
