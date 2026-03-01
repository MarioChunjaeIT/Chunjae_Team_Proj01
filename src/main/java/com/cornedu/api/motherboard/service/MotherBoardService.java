package com.cornedu.api.motherboard.service;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import com.cornedu.api.motherboard.dto.MotherBoardDto;
import com.cornedu.api.motherboard.dto.MotherBoardRequest;
import com.cornedu.api.motherboard.mapper.MotherBoardMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotherBoardService {

    private final MotherBoardMapper mapper;

    public ApiResponse<List<MotherBoardDto>> list(PageRequest page) {
        String keyword = page.hasSearch() ? page.getKeyword() : null;
        String searchType = page.hasSearch() ? page.getSearchType() : null;
        List<MotherBoardDto> list = mapper.findAll(page.getSize(), page.getOffset(), keyword, searchType);
        long total = mapper.count(keyword, searchType);
        return ApiResponse.ok(list, ApiResponse.PageInfo.builder()
                .page(page.getPage()).size(page.getSize())
                .totalElements(total).totalPages((int) Math.ceil((double) total / page.getSize()))
                .build());
    }

    public MotherBoardDto get(int bno) {
        mapper.incrementCnt(bno);
        MotherBoardDto dto = mapper.findByBno(bno);
        if (dto == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        return dto;
    }

    public void create(MotherBoardRequest req, String author) {
        MotherBoardDto dto = new MotherBoardDto();
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        dto.setAuthor(author);
        mapper.insert(dto);
    }

    public void update(int bno, MotherBoardRequest req) {
        MotherBoardDto dto = new MotherBoardDto();
        dto.setBno(bno);
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        mapper.update(dto);
    }

    public void delete(int bno) { mapper.deleteByBno(bno); }
}
