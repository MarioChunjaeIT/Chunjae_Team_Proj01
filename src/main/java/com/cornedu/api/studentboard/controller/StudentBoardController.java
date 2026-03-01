package com.cornedu.api.studentboard.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.dto.PageRequest;
import com.cornedu.api.studentboard.dto.StudentBoardDto;
import com.cornedu.api.studentboard.dto.StudentBoardRequest;
import com.cornedu.api.studentboard.service.StudentBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "StudentBoard", description = "학생 커뮤니티 API")
@RestController
@RequestMapping("/api/studentboards")
@RequiredArgsConstructor
public class StudentBoardController {

    private final StudentBoardService service;

    @Operation(summary = "목록") @GetMapping
    public ApiResponse<List<StudentBoardDto>> list(PageRequest page) { return service.list(page); }

    @Operation(summary = "상세") @GetMapping("/{bno}")
    public ApiResponse<StudentBoardDto> get(@PathVariable int bno) { return ApiResponse.ok(service.get(bno)); }

    @Operation(summary = "작성 (관리자/학생)") @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ApiResponse<Void> create(@Valid @RequestBody StudentBoardRequest req, Authentication auth) {
        service.create(req, auth.getName()); return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "수정 (관리자/학생)") @PutMapping("/{bno}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ApiResponse<Void> update(@PathVariable int bno, @Valid @RequestBody StudentBoardRequest req) {
        service.update(bno, req); return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "삭제 (관리자/학생)") @DeleteMapping("/{bno}")
    @PreAuthorize("hasAnyRole('ADMIN','STUDENT')")
    public ApiResponse<Void> delete(@PathVariable int bno) { service.delete(bno); return ApiResponse.ok("삭제 완료"); }
}
