package com.cornedu.api.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface TokenBlacklistMapper {
    void insert(@Param("token") String token, @Param("expiresAt") java.time.LocalDateTime expiresAt);
    long countByToken(@Param("token") String token);
    void deleteExpired();
}
