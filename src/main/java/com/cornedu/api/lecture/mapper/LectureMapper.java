package com.cornedu.api.lecture.mapper;

import com.cornedu.api.lecture.dto.LectureDto;
import com.cornedu.api.lecture.dto.LectureVideoDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface LectureMapper {
    List<LectureDto> findAll(@Param("target") String target);
    long count(@Param("target") String target);
    LectureDto findByLno(@Param("lno") int lno);
    List<LectureVideoDto> findVideosByLno(@Param("lno") int lno);
    int insert(LectureDto dto);
    int update(LectureDto dto);
    int deleteByLno(@Param("lno") int lno);
    int insertVideo(LectureVideoDto dto);
    int updateVideo(LectureVideoDto dto);
    int deleteVideoByVno(@Param("vno") int vno);
}
