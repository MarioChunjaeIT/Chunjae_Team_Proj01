package com.cornedu.api.qna.service;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import com.cornedu.api.common.service.MailService;
import com.cornedu.api.member.mapper.MemberMapper;
import com.cornedu.api.member.dto.MemberDto;
import com.cornedu.api.qna.dto.QnaDto;
import com.cornedu.api.qna.dto.QnaRequest;
import com.cornedu.api.qna.mapper.QnaMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QnaService {

    private final QnaMapper mapper;
    private final MailService mailService;
    private final MemberMapper memberMapper;

    public ApiResponse<List<QnaDto>> list(PageRequest page) {
        String keyword = page.hasSearch() ? page.getKeyword() : null;
        String searchType = page.hasSearch() ? page.getSearchType() : null;
        List<QnaDto> list = mapper.findAll(page.getSize(), page.getOffset(), keyword, searchType);
        long total = mapper.count(keyword, searchType);
        return ApiResponse.ok(list, ApiResponse.PageInfo.builder()
                .page(page.getPage()).size(page.getSize())
                .totalElements(total).totalPages((int) Math.ceil((double) total / page.getSize()))
                .build());
    }

    public QnaDto get(int qno) {
        mapper.incrementCnt(qno);
        QnaDto dto = mapper.findByQno(qno);
        if (dto == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        return dto;
    }

    public void create(QnaRequest req, String author) {
        QnaDto dto = new QnaDto();
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        dto.setAuthor(author);
        dto.setLev(0);
        dto.setPar(0);
        mapper.insert(dto);
    }

    public void answer(int qno, QnaRequest req, String author) {
        QnaDto parent = mapper.findByQno(qno);
        QnaDto dto = new QnaDto();
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        dto.setAuthor(author);
        dto.setLev(1);
        dto.setPar(qno);
        mapper.insert(dto);
        if (parent != null && parent.getAuthor() != null) {
            MemberDto member = memberMapper.findById(parent.getAuthor());
            if (member != null && member.getEmail() != null && !member.getEmail().isBlank()) {
                mailService.sendQnaReplyNotification(member.getEmail(), parent.getTitle());
            }
        }
    }

    public void update(int qno, QnaRequest req) {
        QnaDto dto = new QnaDto();
        dto.setQno(qno);
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        mapper.update(dto);
    }

    public void delete(int qno) { mapper.deleteByQno(qno); }
}
