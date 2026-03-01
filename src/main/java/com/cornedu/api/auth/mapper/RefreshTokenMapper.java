package com.cornedu.api.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RefreshTokenMapper {
    void upsert(@Param("memberId") String memberId, @Param("token") String token, @Param("expiresAt") java.time.LocalDateTime expiresAt);
    String findTokenByMemberId(@Param("memberId") String memberId);
    void deleteByMemberId(@Param("memberId") String memberId);
}
