package com.cornedu.api.member.mapper;

import com.cornedu.api.member.dto.MemberDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MemberMapper {
    MemberDto findById(@Param("id") String id);
    List<MemberDto> findAll(@Param("keyword") String keyword);
    int insert(MemberDto member);
    int update(MemberDto member);
    int deleteById(@Param("id") String id);
    int countById(@Param("id") String id);
    int countByEmail(@Param("email") String email);
    String findIdByEmail(@Param("email") String email);
    int updatePw(@Param("id") String id, @Param("pw") String pw);
    int updateProfileImagePath(@Param("id") String id, @Param("profileImagePath") String profileImagePath);
    int updateStatus(@Param("id") String id, @Param("status") String status);
    int updatePoint(@Param("id") String id, @Param("amount") int amount);
    long countAll();
}
