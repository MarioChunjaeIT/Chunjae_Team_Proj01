package com.cornedu.api.member.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.common.util.FileStorageService;
import com.cornedu.api.member.dto.MemberDto;
import com.cornedu.api.member.dto.MemberUpdateRequest;
import com.cornedu.api.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Tag(name = "Member", description = "회원 API")
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FileStorageService fileStorage;

    @Operation(summary = "내 프로필 조회")
    @GetMapping("/me")
    public ApiResponse<MemberDto> me(Authentication auth) {
        return ApiResponse.ok(memberService.getProfile(auth.getName()));
    }

    @Operation(summary = "프로필 수정")
    @PutMapping("/me")
    public ApiResponse<Void> update(Authentication auth, @RequestBody MemberUpdateRequest request) {
        memberService.updateProfile(auth.getName(), request);
        return ApiResponse.ok("수정 완료");
    }

    @Operation(summary = "프로필 이미지 업로드")
    @PostMapping("/me/profile-image")
    public ApiResponse<Map<String, String>> uploadProfileImage(Authentication auth, @RequestParam("file") MultipartFile file) {
        String filename = fileStorage.store(file);
        if (filename != null) memberService.updateProfileImage(auth.getName(), filename);
        return ApiResponse.ok(Map.of("profileImagePath", filename != null ? filename : ""));
    }

    @Operation(summary = "회원 목록 (관리자)")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<List<MemberDto>> list(@RequestParam(required = false) String keyword) {
        return ApiResponse.ok(memberService.findAll(keyword));
    }

    @Operation(summary = "회원 삭제 (관리자)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> delete(@PathVariable String id) {
        memberService.delete(id);
        return ApiResponse.ok("삭제 완료");
    }

    @Operation(summary = "회원 정지/강퇴 (관리자)")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> setStatus(@PathVariable String id, @RequestParam String status) {
        memberService.setStatus(id, status);
        return ApiResponse.ok("처리 완료");
    }

    @Operation(summary = "포인트 적립 (관리자)")
    @PostMapping("/{id}/point")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<Void> addPoint(@PathVariable String id, @RequestBody java.util.Map<String, Object> body) {
        int amount = (Integer) body.getOrDefault("amount", 0);
        String reason = (String) body.getOrDefault("reason", "적립");
        memberService.addPoint(id, amount, reason);
        return ApiResponse.ok("적립 완료");
    }

    @Operation(summary = "포인트 사용")
    @PostMapping("/me/point/use")
    public ApiResponse<Void> usePoint(Authentication auth, @RequestBody java.util.Map<String, Object> body) {
        int amount = (Integer) body.getOrDefault("amount", 0);
        String reason = (String) body.getOrDefault("reason", "사용");
        memberService.usePoint(auth.getName(), amount, reason);
        return ApiResponse.ok("사용 완료");
    }
}
