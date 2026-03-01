package com.cornedu.api.faq.service;

import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import com.cornedu.api.faq.dto.FaqDto;
import com.cornedu.api.faq.mapper.FaqMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FaqService {

    private final FaqMapper mapper;

    public List<FaqDto> list() { return mapper.findAll(); }

    public FaqDto get(int fno) {
        mapper.incrementCnt(fno);
        FaqDto dto = mapper.findByFno(fno);
        if (dto == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        return dto;
    }

    public void create(FaqDto dto) {
        mapper.insert(dto);
    }

    public void update(int fno, FaqDto dto) {
        FaqDto existing = mapper.findByFno(fno);
        if (existing == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        dto.setFno(fno);
        mapper.update(dto);
    }

    public void delete(int fno) {
        if (mapper.findByFno(fno) == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        mapper.deleteByFno(fno);
    }
}
