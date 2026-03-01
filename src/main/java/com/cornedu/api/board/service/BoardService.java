package com.cornedu.api.board.service;

import com.cornedu.api.board.dto.BoardDto;
import com.cornedu.api.board.dto.BoardRequest;
import com.cornedu.api.board.mapper.BoardMapper;
import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardMapper boardMapper;

    public ApiResponse<List<BoardDto>> list(PageRequest page) {
        String keyword = page.hasSearch() ? page.getKeyword() : null;
        String searchType = page.hasSearch() ? page.getSearchType() : null;
        List<BoardDto> list = boardMapper.findAll(page.getSize(), page.getOffset(), keyword, searchType);
        long total = boardMapper.count(keyword, searchType);
        return ApiResponse.ok(list, ApiResponse.PageInfo.builder()
                .page(page.getPage()).size(page.getSize())
                .totalElements(total).totalPages((int) Math.ceil((double) total / page.getSize()))
                .build());
    }

    public BoardDto get(int bno) {
        boardMapper.incrementCnt(bno);
        BoardDto dto = boardMapper.findByBno(bno);
        if (dto == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        return dto;
    }

    public void create(BoardRequest req, String author) {
        BoardDto dto = new BoardDto();
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        dto.setAuthor(author);
        boardMapper.insert(dto);
    }

    public void update(int bno, BoardRequest req) {
        BoardDto dto = new BoardDto();
        dto.setBno(bno);
        dto.setTitle(req.getTitle());
        dto.setContent(req.getContent());
        boardMapper.update(dto);
    }

    public void delete(int bno) {
        boardMapper.deleteByBno(bno);
    }

    public void setFixed(int bno, int fixed) {
        if (boardMapper.findByBno(bno) == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        boardMapper.updateFixed(bno, fixed);
    }
}
