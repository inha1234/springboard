package com.springboard.dto.post;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PostCustomPageDto<T> {
    private List<T> content;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    public PostCustomPageDto(Page<T> page) {
        this.content = page.getContent();
        this.currentPage = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.first = page.isFirst();
        this.last = page.isLast();
    }
}
