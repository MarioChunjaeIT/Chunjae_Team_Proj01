package com.cornedu.api.comment.dto;

import lombok.Data;

@Data
public class CommentDto {
    private int cno;
    private int bno;
    private String author;
    private String resdate;
    private String content;
}
