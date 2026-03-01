package com.cornedu.api.lecture.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureVideoRequest {
    private int lno;
    private String vTitle;
    private String filePath;
    private String duration;
}
