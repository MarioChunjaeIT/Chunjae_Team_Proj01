package com.cornedu.api.lecture.dto;

import lombok.Data;

@Data
public class LectureVideoDto {
    private int vno;
    private int lno;
    private String vTitle;
    private String filePath;
    private String duration;
}
