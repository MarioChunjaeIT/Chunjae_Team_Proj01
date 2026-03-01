package com.cornedu.api.qna.dto;

import lombok.Data;

@Data
public class QnaDto {
    private int qno;
    private String title;
    private String content;
    private String author;
    private String name;
    private String resdate;
    private int cnt;
    private int lev;
    private int par;
}
