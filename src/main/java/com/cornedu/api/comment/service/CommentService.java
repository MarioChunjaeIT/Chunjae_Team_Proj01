package com.cornedu.api.comment.service;

import com.cornedu.api.comment.dto.CommentDto;
import com.cornedu.api.comment.dto.CommentRequest;
import com.cornedu.api.comment.mapper.CommentMapper;
import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentMapper mapper;

    private static final Map<String, String> TABLE_MAP = Map.of(
            "motherboards", "tbl_mother_comment",
            "studentboards", "tbl_student_comment",
            "boards", "tbl_board_comment"
    );

    public List<CommentDto> list(String boardType, int bno) {
        return mapper.findByBno(resolveTable(boardType), bno);
    }

    public void create(String boardType, int bno, CommentRequest req, String author) {
        CommentDto dto = new CommentDto();
        dto.setBno(bno);
        dto.setContent(req.getContent());
        dto.setAuthor(author);
        mapper.insert(resolveTable(boardType), dto);
    }

    public void delete(String boardType, int cno) {
        mapper.deleteByCno(resolveTable(boardType), cno);
    }

    private String resolveTable(String boardType) {
        String table = TABLE_MAP.get(boardType);
        if (table == null) throw new BusinessException(ErrorCode.INVALID_INPUT, "잘못된 게시판 타입: " + boardType);
        return table;
    }
}
