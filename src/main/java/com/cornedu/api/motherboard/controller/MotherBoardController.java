package com.cornedu.api.motherboard.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.motherboard.dto.MotherBoardDto;
import com.cornedu.api.motherboard.dto.MotherBoardRequest;
import com.cornedu.api.motherboard.service.MotherBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "MotherBoard", description = "학부모 커뮤니티 API")
@RestController
@RequestMapping("/api/motherboards")
@RequiredArgsConstructor
public class MotherBoardController {

    private final MotherBoardService service;

    @Operation(summary = "목록") @GetMapping
    public ApiResponse<List<MotherBoardDto>> list(PageRequest page) { return service.list(page); }

    @Operation(summary = "상세") @GetMapping("/{bno}")
    public ApiResponse<MotherBoardDto> get(@PathVariable int bno) { return ApiResponse.ok(service.get(bno)); }

    @Operation(summary = "작성 (관리자/학부모)") @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public ApiResponse<Void> create(@Valid @RequestBody MotherBoardRequest req, Authentication auth) {
        service.create(req, auth.getName()); return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "수정 (관리자/학부모)") @PutMapping("/{bno}")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public ApiResponse<Void> update(@PathVariable int bno, @Valid @RequestBody MotherBoardRequest req) {
        service.update(bno, req); return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "삭제 (관리자/학부모)") @DeleteMapping("/{bno}")
    @PreAuthorize("hasAnyRole('ADMIN','PARENT')")
    public ApiResponse<Void> delete(@PathVariable int bno) { service.delete(bno); return ApiResponse.ok("삭제 완료"); }
}
