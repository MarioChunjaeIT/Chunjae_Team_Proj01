package com.cornedu.api.faq.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.faq.dto.FaqDto;
import com.cornedu.api.faq.service.FaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "FAQ", description = "자주 묻는 질문 API")
@RestController
@RequestMapping("/api/faq")
@RequiredArgsConstructor
public class FaqController {

    private final FaqService service;

    @Operation(summary = "FAQ 목록") @GetMapping
    public ApiResponse<List<FaqDto>> list() { return ApiResponse.ok(service.list()); }

    @Operation(summary = "FAQ 상세") @GetMapping("/{fno}")
    public ApiResponse<FaqDto> get(@PathVariable int fno) { return ApiResponse.ok(service.get(fno)); }

    @Operation(summary = "FAQ 등록 (관리자)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> create(@Valid @RequestBody FaqDto dto) {
        service.create(dto);
        return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "FAQ 수정 (관리자)")
    @PutMapping("/{fno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> update(@PathVariable int fno, @Valid @RequestBody FaqDto dto) {
        service.update(fno, dto);
        return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "FAQ 삭제 (관리자)")
    @DeleteMapping("/{fno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable int fno) {
        service.delete(fno);
        return ApiResponse.ok("삭제 완료");
    }
}
