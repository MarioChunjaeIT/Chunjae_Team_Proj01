package com.cornedu.api.lecture.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureRequest {
    private String lectureName;
    private String teacher;
    private String content;
    private String filePath;
    private String target;
}
