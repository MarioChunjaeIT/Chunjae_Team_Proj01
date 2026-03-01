package com.cornedu.api.board.mapper;

import com.cornedu.api.board.dto.BoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardDto> findAll(@Param("size") int size, @Param("offset") int offset,
                          @Param("keyword") String keyword, @Param("searchType") String searchType);
    long count(@Param("keyword") String keyword, @Param("searchType") String searchType);
    BoardDto findByBno(@Param("bno") int bno);
    int insert(BoardDto board);
    int update(BoardDto board);
    int deleteByBno(@Param("bno") int bno);
    void incrementCnt(@Param("bno") int bno);
    int updateFixed(@Param("bno") int bno, @Param("fixed") int fixed);
}
