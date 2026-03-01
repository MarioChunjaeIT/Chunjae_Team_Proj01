package com.cornedu.api.lecture.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.util.FileStorageService;
import com.cornedu.api.lecture.dto.*;
import com.cornedu.api.lecture.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Lecture", description = "강의 API")
@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

    private final LectureService service;
    private final FileStorageService fileStorage;

    @Operation(summary = "강의 목록") @GetMapping
    public ApiResponse<List<LectureDto>> list(@RequestParam(required = false) String target) {
        return ApiResponse.ok(service.list(target));
    }

    @Operation(summary = "강의 상세") @GetMapping("/{lno}")
    public ApiResponse<LectureDto> get(@PathVariable int lno) { return ApiResponse.ok(service.get(lno)); }

    @Operation(summary = "강의 영상 목록") @GetMapping("/{lno}/videos")
    public ApiResponse<List<LectureVideoDto>> videos(@PathVariable int lno) {
        return ApiResponse.ok(service.getVideos(lno));
    }

    @Operation(summary = "파일 업로드 (관리자)")
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Map<String, String>> upload(@RequestParam("file") MultipartFile file) {
        String filename = fileStorage.store(file);
        return ApiResponse.ok(Map.of("filePath", filename != null ? filename : ""));
    }

    @Operation(summary = "강의 등록 (관리자)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> create(@RequestBody LectureRequest req) {
        LectureDto dto = new LectureDto();
        dto.setLectureName(req.getLectureName());
        dto.setTeacher(req.getTeacher());
        dto.setContent(req.getContent());
        dto.setFilePath(req.getFilePath());
        dto.setTarget(req.getTarget());
        service.create(dto);
        return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "강의 수정 (관리자)")
    @PutMapping("/{lno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> update(@PathVariable int lno, @RequestBody LectureRequest req) {
        LectureDto dto = new LectureDto();
        dto.setLno(lno);
        dto.setLectureName(req.getLectureName());
        dto.setTeacher(req.getTeacher());
        dto.setContent(req.getContent());
        dto.setFilePath(req.getFilePath());
        dto.setTarget(req.getTarget());
        service.update(lno, dto);
        return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "강의 삭제 (관리자)")
    @DeleteMapping("/{lno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable int lno) {
        service.delete(lno);
        return ApiResponse.ok("삭제 완료");
    }

    @Operation(summary = "영상 등록 (관리자)")
    @PostMapping("/videos")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> createVideo(@RequestBody LectureVideoRequest req) {
        LectureVideoDto dto = new LectureVideoDto();
        dto.setLno(req.getLno());
        dto.setVTitle(req.getVTitle());
        dto.setFilePath(req.getFilePath());
        dto.setDuration(req.getDuration());
        service.createVideo(dto);
        return ApiResponse.ok("등록 완료");
    }

    @Operation(summary = "영상 수정 (관리자)")
    @PutMapping("/videos/{vno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> updateVideo(@PathVariable int vno, @RequestBody LectureVideoRequest req) {
        LectureVideoDto dto = new LectureVideoDto();
        dto.setVno(vno);
        dto.setLno(req.getLno());
        dto.setVTitle(req.getVTitle());
        dto.setFilePath(req.getFilePath());
        dto.setDuration(req.getDuration());
        service.updateVideo(vno, dto);
        return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "영상 삭제 (관리자)")
    @DeleteMapping("/videos/{vno}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> deleteVideo(@PathVariable int vno) {
        service.deleteVideo(vno);
        return ApiResponse.ok("삭제 완료");
    }
}
