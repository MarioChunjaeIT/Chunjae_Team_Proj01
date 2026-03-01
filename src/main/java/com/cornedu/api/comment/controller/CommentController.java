package com.cornedu.api.comment.controller;

import com.cornedu.api.comment.dto.CommentDto;
import com.cornedu.api.comment.dto.CommentRequest;
import com.cornedu.api.comment.service.CommentService;
import com.cornedu.api.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Comment", description = "댓글 API")
@RestController
@RequestMapping("/api/{boardType}/{bno}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

    @Operation(summary = "댓글 목록")
    @GetMapping
    public ApiResponse<List<CommentDto>> list(@PathVariable String boardType, @PathVariable int bno) {
        return ApiResponse.ok(service.list(boardType, bno));
    }

    @Operation(summary = "댓글 작성")
    @PostMapping
    public ApiResponse<Void> create(@PathVariable String boardType, @PathVariable int bno,
                                    @Valid @RequestBody CommentRequest req, Authentication auth) {
        service.create(boardType, bno, req, auth.getName());
        return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{cno}")
    public ApiResponse<Void> delete(@PathVariable String boardType, @PathVariable int bno, @PathVariable int cno) {
        service.delete(boardType, cno);
        return ApiResponse.ok("삭제 완료");
    }
}
