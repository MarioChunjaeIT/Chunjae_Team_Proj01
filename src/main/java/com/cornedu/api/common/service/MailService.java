package com.cornedu.api.common.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {

    private final JavaMailSender mailSender;

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("[CORN EDU] 비밀번호 재설정");
            msg.setText("비밀번호 재설정을 요청하셨습니다. 아래 링크를 클릭하여 새 비밀번호를 설정하세요.\n\n" + resetLink + "\n\n유효 시간은 30분입니다.");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Failed to send password reset email: {}", e.getMessage());
            throw e;
        }
    }

    public void sendQnaReplyNotification(String toEmail, String qnaTitle) {
        try {
            SimpleMailMessage msg = new SimpleMailMessage();
            msg.setTo(toEmail);
            msg.setSubject("[CORN EDU] QnA 답변이 등록되었습니다");
            msg.setText("문의하신 \"" + qnaTitle + "\"에 답변이 등록되었습니다. 로그인하여 확인해 주세요.");
            mailSender.send(msg);
        } catch (Exception e) {
            log.warn("Failed to send QnA notification: {}", e.getMessage());
        }
    }
}
