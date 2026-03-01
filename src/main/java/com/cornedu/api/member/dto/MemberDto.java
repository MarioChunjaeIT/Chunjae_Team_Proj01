package com.cornedu.api.member.dto;

import lombok.Data;

@Data
public class MemberDto {
    private String id;
    private String pw;
    private String name;
    private String email;
    private String tel;
    private String resdate;
    private int point;
    private int per;
    private String profileImagePath;
    private String status;
}
