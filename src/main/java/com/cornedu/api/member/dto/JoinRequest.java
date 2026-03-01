package com.cornedu.api.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JoinRequest {
    @NotBlank private String id;
    @NotBlank private String pw;
    @NotBlank private String name;
    @NotBlank private String email;
    @NotBlank private String tel;
    private int per;
}
