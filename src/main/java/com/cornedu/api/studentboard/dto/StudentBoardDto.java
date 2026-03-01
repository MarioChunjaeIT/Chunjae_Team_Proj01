package com.cornedu.api.studentboard.dto;

import lombok.Data;

@Data
public class StudentBoardDto {
    private int bno;
    private String title;
    private String content;
    private String author;
    private String resdate;
    private int cnt;
    private int fixed;
}
