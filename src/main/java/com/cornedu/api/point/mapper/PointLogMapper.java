package com.cornedu.api.point.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface PointLogMapper {
    void insert(@Param("memberId") String memberId, @Param("amount") int amount, @Param("reason") String reason);
}
