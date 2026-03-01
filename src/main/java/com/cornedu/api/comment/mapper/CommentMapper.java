package com.cornedu.api.comment.mapper;

import com.cornedu.api.comment.dto.CommentDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentMapper {
    List<CommentDto> findByBno(@Param("table") String table, @Param("bno") int bno);
    int insert(@Param("table") String table, @Param("comment") CommentDto comment);
    int deleteByCno(@Param("table") String table, @Param("cno") int cno);
}
