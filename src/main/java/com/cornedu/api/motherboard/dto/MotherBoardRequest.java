package com.cornedu.api.motherboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MotherBoardRequest {
    @NotBlank private String title;
    @NotBlank private String content;
}
