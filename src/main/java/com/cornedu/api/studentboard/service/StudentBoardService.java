package com.cornedu.api.studentboard.service;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import com.cornedu.api.studentboard.dto.StudentBoardDto;
import com.cornedu.api.studentboard.dto.StudentBoardRequest;
import com.cornedu.api.studentboard.mapper.StudentBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentBoardService {

    private final StudentBoardMapper mapper;

    public ApiResponse<List<StudentBoardDto>> list(PageRequest page) {
        String keyword = page.hasSearch() ? page.getKeyword() : null;
        String searchType = page.hasSearch() ? page.getSearchType() : null;
        List<StudentBoardDto> list = mapper.findAll(page.getSize(), page.getOffset(), keyword, searchType);
        long total = mapper.count(keyword, searchType);
        return ApiResponse.ok(list, ApiResponse.PageInfo.builder()
                .page(page.getPage()).size(page.getSize())
                .totalElements(total).totalPages((int) Math.ceil((double) total / page.getSize()))
                .build());
    }

    public StudentBoardDto get(int bno) {
        mapper.incrementCnt(bno);
        StudentBoardDto dto = mapper.findByBno(bno);
        if (dto == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        return dto;
    }

    public void create(StudentBoardRequest req, String author) {
        StudentBoardDto dto = new StudentBoardDto();
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        dto.setAuthor(author);
        mapper.insert(dto);
    }

    public void update(int bno, StudentBoardRequest req) {
        StudentBoardDto dto = new StudentBoardDto();
        dto.setBno(bno);
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        mapper.update(dto);
    }

    public void delete(int bno) { mapper.deleteByBno(bno); }
}
