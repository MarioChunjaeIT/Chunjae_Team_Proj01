package com.cornedu.api.lecture.dto;

import lombok.Data;

@Data
public class LectureDto {
    private int lno;
    private String lectureName;
    private String teacher;
    private String content;
    private String filePath;
    private String target;
}
