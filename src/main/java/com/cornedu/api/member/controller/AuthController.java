package com.cornedu.api.member.controller;

import com.cornedu.api.common.dto.ApiResponse;
import com.cornedu.api.member.dto.ForgotPasswordRequest;
import com.cornedu.api.member.dto.JoinRequest;
import com.cornedu.api.member.dto.LoginRequest;
import com.cornedu.api.member.dto.LoginResponse;
import com.cornedu.api.member.dto.PasswordChangeRequest;
import com.cornedu.api.member.dto.RefreshRequest;
import com.cornedu.api.member.dto.ResetPasswordRequest;
import com.cornedu.api.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "인증 API")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        return ApiResponse.ok(memberService.login(request));
    }

    @Operation(summary = "회원가입")
    @PostMapping("/join")
    public ApiResponse<Void> join(@Valid @RequestBody JoinRequest request) {
        memberService.join(request);
        return ApiResponse.ok("회원가입 성공");
    }

    @Operation(summary = "토큰 갱신")
    @PostMapping("/refresh")
    public ApiResponse<LoginResponse> refresh(@Valid @RequestBody RefreshRequest request) {
        return ApiResponse.ok(memberService.refresh(request.getRefreshToken()));
    }

    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ApiResponse<Void> logout(Authentication auth, @RequestHeader(value = "Authorization", required = false) String authorization) {
        if (authorization != null && authorization.startsWith("Bearer ")) {
            memberService.logout(authorization.substring(7));
        }
        return ApiResponse.ok("로그아웃 완료");
    }

    @Operation(summary = "아이디 중복 체크")
    @GetMapping("/check-id")
    public ApiResponse<Map<String, Boolean>> checkId(@RequestParam String id) {
        return ApiResponse.ok(Map.of("exists", memberService.checkIdExists(id)));
    }

    @Operation(summary = "이메일 중복 체크")
    @GetMapping("/check-email")
    public ApiResponse<Map<String, Boolean>> checkEmail(@RequestParam String email) {
        return ApiResponse.ok(Map.of("exists", memberService.checkEmailExists(email)));
    }

    @Operation(summary = "비밀번호 변경 (로그인 필요)")
    @PostMapping("/change-password")
    public ApiResponse<Void> changePassword(Authentication auth, @Valid @RequestBody PasswordChangeRequest request) {
        memberService.changePassword(auth.getName(), request);
        return ApiResponse.ok("비밀번호가 변경되었습니다.");
    }

    @Operation(summary = "비밀번호 찾기 (이메일로 재설정 링크 발송)")
    @PostMapping("/forgot-password")
    public ApiResponse<Void> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        memberService.requestPasswordReset(request.getEmail());
        return ApiResponse.ok("등록된 이메일로 재설정 링크를 발송했습니다.");
    }

    @Operation(summary = "비밀번호 재설정 (토큰으로 새 비밀번호 설정)")
    @PostMapping("/reset-password")
    public ApiResponse<Void> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        memberService.resetPassword(request.getToken(), request.getNewPw());
        return ApiResponse.ok("비밀번호가 재설정되었습니다.");
    }
}
