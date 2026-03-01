package com.cornedu.api.motherboard.dto;

import lombok.Data;

@Data
public class MotherBoardDto {
    private int bno;
    private String title;
    private String content;
    private String author;
    private String resdate;
    private int cnt;
    private int fixed;
}
