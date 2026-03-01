package com.cornedu.api.qna.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.qna.dto.QnaDto;
import com.cornedu.api.qna.dto.QnaRequest;
import com.cornedu.api.qna.service.QnaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "QnA", description = "질문답변 API")
@RestController
@RequestMapping("/api/qna")
@RequiredArgsConstructor
public class QnaController {

    private final QnaService service;

    @Operation(summary = "목록") @GetMapping
    public ApiResponse<List<QnaDto>> list(PageRequest page) { return service.list(page); }

    @Operation(summary = "상세") @GetMapping("/{qno}")
    public ApiResponse<QnaDto> get(@PathVariable int qno) { return ApiResponse.ok(service.get(qno)); }

    @Operation(summary = "질문 작성") @PostMapping
    public ApiResponse<Void> create(@Valid @RequestBody QnaRequest req, Authentication auth) {
        service.create(req, auth.getName()); return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "답변 작성 (관리자)") @PostMapping("/{qno}/answer")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> answer(@PathVariable int qno, @Valid @RequestBody QnaRequest req, Authentication auth) {
        service.answer(qno, req, auth.getName()); return ApiResponse.ok("답변 등록 완료");
    }

    @Operation(summary = "수정") @PutMapping("/{qno}")
    public ApiResponse<Void> update(@PathVariable int qno, @Valid @RequestBody QnaRequest req) {
        service.update(qno, req); return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "삭제") @DeleteMapping("/{qno}")
    public ApiResponse<Void> delete(@PathVariable int qno) { service.delete(qno); return ApiResponse.ok("삭제 완료"); }
}
