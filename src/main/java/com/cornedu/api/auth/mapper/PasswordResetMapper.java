package com.cornedu.api.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PasswordResetMapper {
    void insert(@Param("memberId") String memberId, @Param("token") String token, @Param("expiresAt") java.time.LocalDateTime expiresAt);
    String findMemberIdByToken(@Param("token") String token);
    void markUsed(@Param("token") String token);
    void deleteExpired();
}
