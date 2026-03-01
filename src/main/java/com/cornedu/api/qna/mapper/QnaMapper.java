package com.cornedu.api.qna.mapper;

import com.cornedu.api.qna.dto.QnaDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface QnaMapper {
    List<QnaDto> findAll(@Param("size") int size, @Param("offset") int offset,
                         @Param("keyword") String keyword, @Param("searchType") String searchType);
    long count(@Param("keyword") String keyword, @Param("searchType") String searchType);
    QnaDto findByQno(@Param("qno") int qno);
    int insert(QnaDto qna);
    int update(QnaDto qna);
    int deleteByQno(@Param("qno") int qno);
    void incrementCnt(@Param("qno") int qno);
}
