package com.cornedu.api.studentboard.mapper;

import com.cornedu.api.studentboard.dto.StudentBoardDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface StudentBoardMapper {
    List<StudentBoardDto> findAll(@Param("size") int size, @Param("offset") int offset,
                                  @Param("keyword") String keyword, @Param("searchType") String searchType);
    long count(@Param("keyword") String keyword, @Param("searchType") String searchType);
    StudentBoardDto findByBno(@Param("bno") int bno);
    int insert(StudentBoardDto board);
    int update(StudentBoardDto board);
    int deleteByBno(@Param("bno") int bno);
    void incrementCnt(@Param("bno") int bno);
}
