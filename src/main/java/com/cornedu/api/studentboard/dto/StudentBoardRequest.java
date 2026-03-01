package com.cornedu.api.studentboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class StudentBoardRequest {
    @NotBlank private String title;
    @NotBlank private String content;
}
