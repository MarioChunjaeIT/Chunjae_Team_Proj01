package com.cornedu.api.motherboard.mapper;

import com.cornedu.api.motherboard.dto.MotherBoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface MotherBoardMapper {
    List<MotherBoardDto> findAll(@Param("size") int size, @Param("offset") int offset,
                                 @Param("keyword") String keyword, @Param("searchType") String searchType);
    long count(@Param("keyword") String keyword, @Param("searchType") String searchType);
    MotherBoardDto findByBno(@Param("bno") int bno);
    int insert(MotherBoardDto board);
    int update(MotherBoardDto board);
    int deleteByBno(@Param("bno") int bno);
    void incrementCnt(@Param("bno") int bno);
}
