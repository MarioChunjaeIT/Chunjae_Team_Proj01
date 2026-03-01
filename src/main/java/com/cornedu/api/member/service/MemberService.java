package com.cornedu.api.member.service;

import com.cornedu.api.auth.mapper.PasswordResetMapper;
import com.cornedu.api.auth.mapper.RefreshTokenMapper;
import com.cornedu.api.auth.mapper.TokenBlacklistMapper;
import com.cornedu.api.common.service.MailService;
import com.cornedu.api.point.mapper.PointLogMapper;
import com.cornedu.api.common.exception.BusinessException;
import com.cornedu.api.common.exception.ErrorCode;
import com.cornedu.api.common.util.Sha256Util;
import com.cornedu.api.member.dto.*;
import com.cornedu.api.member.mapper.MemberMapper;
import com.cornedu.api.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberMapper memberMapper;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenMapper refreshTokenMapper;
    private final TokenBlacklistMapper tokenBlacklistMapper;
    private final PasswordResetMapper passwordResetMapper;
    private final MailService mailService;
    private final PointLogMapper pointLogMapper;

    @Value("${jwt.refresh-expiration:604800000}")
    private long refreshExpirationMs;
    @Value("${app.password-reset-expiration-minutes:30}")
    private int resetExpirationMinutes;
    @Value("${app.password-reset-base-url:http://localhost:3000/reset-password}")
    private String resetBaseUrl;

    public LoginResponse login(LoginRequest request) {
        MemberDto member = memberMapper.findById(request.getId());
        if (member == null || !member.getPw().equals(Sha256Util.encrypt(request.getPw()))) {
            throw new BusinessException(ErrorCode.LOGIN_FAILED);
        }
        if ("INACTIVE".equals(member.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN);
        }
        String role = resolveRole(member);
        String token = tokenProvider.createToken(member.getId(), role);
        String refreshToken = tokenProvider.createRefreshToken(member.getId());
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                new Date(System.currentTimeMillis() + refreshExpirationMs).toInstant(), ZoneId.systemDefault());
        refreshTokenMapper.upsert(member.getId(), refreshToken, expiresAt);
        return new LoginResponse(token, refreshToken, member.getId(), member.getName(), role);
    }

    public LoginResponse refresh(String refreshTokenValue) {
        if (!tokenProvider.validate(refreshTokenValue) || !tokenProvider.isRefreshToken(refreshTokenValue)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        String memberId = tokenProvider.getId(refreshTokenValue);
        String stored = refreshTokenMapper.findTokenByMemberId(memberId);
        if (stored == null || !stored.equals(refreshTokenValue)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        MemberDto member = memberMapper.findById(memberId);
        if (member == null || "INACTIVE".equals(member.getStatus())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        String role = resolveRole(member);
        String token = tokenProvider.createToken(memberId, role);
        String newRefresh = tokenProvider.createRefreshToken(memberId);
        LocalDateTime expiresAt = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(System.currentTimeMillis() + refreshExpirationMs), ZoneId.systemDefault());
        refreshTokenMapper.upsert(memberId, newRefresh, expiresAt);
        return new LoginResponse(token, newRefresh, memberId, member.getName(), role);
    }

    public void logout(String accessToken) {
        if (accessToken == null || !tokenProvider.validate(accessToken)) return;
        Date exp = tokenProvider.getExpiration(accessToken);
        LocalDateTime expiresAt = LocalDateTime.ofInstant(exp.toInstant(), ZoneId.systemDefault());
        tokenBlacklistMapper.insert(accessToken, expiresAt);
    }

    public void changePassword(String id, PasswordChangeRequest request) {
        MemberDto member = memberMapper.findById(id);
        if (member == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        String currentHashed = Sha256Util.encrypt(request.getCurrentPw());
        if (!member.getPw().equals(currentHashed)) {
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }
        memberMapper.updatePw(id, Sha256Util.encrypt(request.getNewPw()));
    }

    public boolean checkIdExists(String id) {
        return memberMapper.countById(id) > 0;
    }

    public boolean checkEmailExists(String email) {
        return memberMapper.countByEmail(email) > 0;
    }

    public void requestPasswordReset(String email) {
        String memberId = memberMapper.findIdByEmail(email);
        if (memberId == null) return;
        String token = UUID.randomUUID().toString().replace("-", "");
        LocalDateTime expiresAt = LocalDateTime.now().plusMinutes(resetExpirationMinutes);
        passwordResetMapper.insert(memberId, token, expiresAt);
        String link = resetBaseUrl + "?token=" + token;
        mailService.sendPasswordResetEmail(memberMapper.findById(memberId).getEmail(), link);
    }

    public void resetPassword(String token, String newPw) {
        String memberId = passwordResetMapper.findMemberIdByToken(token);
        if (memberId == null) throw new BusinessException(ErrorCode.INVALID_INPUT);
        memberMapper.updatePw(memberId, Sha256Util.encrypt(newPw));
        passwordResetMapper.markUsed(token);
    }

    public void addPoint(String memberId, int amount, String reason) {
        if (amount <= 0) throw new BusinessException(ErrorCode.INVALID_INPUT);
        memberMapper.updatePoint(memberId, amount);
        pointLogMapper.insert(memberId, amount, reason != null ? reason : "적립");
    }

    public void usePoint(String memberId, int amount, String reason) {
        if (amount <= 0) throw new BusinessException(ErrorCode.INVALID_INPUT);
        MemberDto member = memberMapper.findById(memberId);
        if (member == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        if (member.getPoint() < amount) throw new BusinessException(ErrorCode.INVALID_INPUT);
        memberMapper.updatePoint(memberId, -amount);
        pointLogMapper.insert(memberId, -amount, reason != null ? reason : "사용");
    }

    public void setStatus(String id, String status) {
        if (memberMapper.findById(id) == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        memberMapper.updateStatus(id, status);
    }

    public void updateProfileImage(String id, String profileImagePath) {
        memberMapper.updateProfileImagePath(id, profileImagePath);
    }

    public void join(JoinRequest request) {
        if (memberMapper.countById(request.getId()) > 0) {
            throw new BusinessException(ErrorCode.DUPLICATE_ID);
        }
        MemberDto dto = new MemberDto();
        dto.setId(request.getId());
        dto.setPw(Sha256Util.encrypt(request.getPw()));
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setTel(request.getTel());
        dto.setPer(request.getPer());
        memberMapper.insert(dto);
    }

    public MemberDto getProfile(String id) {
        MemberDto member = memberMapper.findById(id);
        if (member == null) throw new BusinessException(ErrorCode.NOT_FOUND);
        member.setPw(null);
        return member;
    }

    public void updateProfile(String id, MemberUpdateRequest request) {
        MemberDto dto = new MemberDto();
        dto.setId(id);
        dto.setName(request.getName());
        dto.setEmail(request.getEmail());
        dto.setTel(request.getTel());
        memberMapper.update(dto);
    }

    public List<MemberDto> findAll(String keyword) {
        List<MemberDto> list = memberMapper.findAll(keyword);
        list.forEach(m -> m.setPw(null));
        return list;
    }

    public void delete(String id) {
        memberMapper.deleteById(id);
    }

    private String resolveRole(MemberDto member) {
        if ("admin".equals(member.getId())) return "ROLE_ADMIN";
        return switch (member.getPer()) {
            case 1 -> "ROLE_STUDENT";
            case 2 -> "ROLE_PARENT";
            default -> "ROLE_USER";
        };
    }
}
