package com.emotion_apiserver.domain.dto;


import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResponseDto<T> {

    private int currentPage;
    private int totalPage;
    private int start;
    private int end;
    private boolean prev;
    private boolean next;
    private int prevPage;
    private int nextPage;
    private List<Integer> pageNumList;
    private List<T> content;

    public PageResponseDto(PageRequestDto pageRequestDto, int totalCount, List<T> content) {
        this.currentPage = pageRequestDto.getPage();
        this.totalPage = (int) Math.ceil((double) totalCount / pageRequestDto.getSize());

        this.end = (int) (Math.ceil(currentPage / 10.0)) * 10;
        this.start = end - 9;

        if (end > totalPage) {
            this.end = totalPage;
        }

        this.prev = start > 1;
        this.next = totalCount > (end * pageRequestDto.getSize());

        this.prevPage = prev ? start - 1 : 0;
        this.nextPage = next ? end + 1 : 0;

        this.pageNumList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
        this.content = content;
    }
}
