package com.cornedu.api.faq.dto;

import lombok.Data;

@Data
public class FaqDto {
    private int fno;
    private String question;
    private String answer;
    private int cnt;
}
