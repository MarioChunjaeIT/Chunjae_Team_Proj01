package com.cornedu.api.faq.mapper;

import com.cornedu.api.faq.dto.FaqDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FaqMapper {
    List<FaqDto> findAll();
    long count();
    FaqDto findByFno(@Param("fno") int fno);
    void incrementCnt(@Param("fno") int fno);
    int insert(FaqDto faq);
    int update(FaqDto faq);
    int deleteByFno(@Param("fno") int fno);
}
