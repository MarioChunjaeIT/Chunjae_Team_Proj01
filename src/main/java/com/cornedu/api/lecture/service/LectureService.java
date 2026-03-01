package com.cornedu.api.lecture.service;

import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import com.cornedu.api.lecture.dto.LectureDto;
import com.cornedu.api.lecture.dto.LectureVideoDto;
import com.cornedu.api.lecture.mapper.LectureMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService {

    private final LectureMapper mapper;

    public List<LectureDto> list(String target) { return mapper.findAll(target); }

    public LectureDto get(int lno) {
        LectureDto dto = mapper.findByLno(lno);
        if (dto == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        return dto;
    }

    public List<LectureVideoDto> getVideos(int lno) { return mapper.findVideosByLno(lno); }

    public void create(LectureDto dto) {
        mapper.insert(dto);
    }

    public void update(int lno, LectureDto dto) {
        if (mapper.findByLno(lno) == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        dto.setLno(lno);
        mapper.update(dto);
    }

    public void delete(int lno) {
        if (mapper.findByLno(lno) == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        mapper.deleteByLno(lno);
    }

    public void createVideo(LectureVideoDto dto) {
        if (mapper.findByLno(dto.getLno()) == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        mapper.insertVideo(dto);
    }

    public void updateVideo(int vno, LectureVideoDto dto) {
        dto.setVno(vno);
        mapper.updateVideo(dto);
    }

    public void deleteVideo(int vno) {
        mapper.deleteVideoByVno(vno);
    }
}
