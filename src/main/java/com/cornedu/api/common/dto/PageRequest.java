package com.cornedu.api.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageRequest {
    private int page = 1;
    private int size = 10;
    /** 검색어 (제목/내용/작성자 검색 시 사용) */
    private String keyword;
    /** title, content, author, all 중 하나. 없으면 검색 안 함 */
    private String searchType;

    public int getOffset() {
        return (page - 1) * size;
    }

    public boolean hasSearch() {
        return keyword != null && !keyword.isBlank() && searchType != null && !searchType.isBlank();
    }
}
