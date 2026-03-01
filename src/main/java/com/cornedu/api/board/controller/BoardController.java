package com.cornedu.api.board.controller;

import com.cornedu.api.board.dto.BoardDto;
import com.cornedu.api.board.dto.BoardRequest;
import com.cornedu.api.board.service.BoardService;
import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Board", description = "공지사항 API")
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "공지사항 목록")
    @GetMapping
    public ApiResponse<List<BoardDto>> list(PageRequest page) {
        return boardService.list(page);
    }

    @Operation(summary = "공지사항 상세")
    @GetMapping("/{bno}")
    public ApiResponse<BoardDto> get(@PathVariable int bno) {
        return ApiResponse.ok(boardService.get(bno));
    }

    @Operation(summary = "공지사항 작성 (관리자)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> create(@Valid @RequestBody BoardRequest req, Authentication auth) {
        boardService.create(req, auth.getName());
        return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "공지사항 수정 (관리자)")
    @PutMapping("/{bno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> update(@PathVariable int bno, @Valid @RequestBody BoardRequest req) {
        boardService.update(bno, req);
        return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "공지사항 삭제 (관리자)")
    @DeleteMapping("/{bno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable int bno) {
        boardService.delete(bno);
        return ApiResponse.ok("삭제 완료");
    }

    @Operation(summary = "공지 고정/해제 (관리자)")
    @PatchMapping("/{bno}/fixed")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> setFixed(@PathVariable int bno, @RequestParam int fixed) {
        boardService.setFixed(bno, fixed);
        return ApiResponse.ok("처리 완료");
    }
}
