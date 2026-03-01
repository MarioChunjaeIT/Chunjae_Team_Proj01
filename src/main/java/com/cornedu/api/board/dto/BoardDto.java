package com.cornedu.api.board.dto;

import lombok.Data;

@Data
public class BoardDto {
    private int bno;
    private String title;
    private String content;
    private String author;
    private String resdate;
    private int cnt;
    private int fixed;
}
