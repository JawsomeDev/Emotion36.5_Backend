package com.emotion_apiserver.service;

import com.emotion_apiserver.domain.Account;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendResetPasswordEmail(String name,String toEmail, String resetLink) {
        Context context = new Context();
        context.setVariable("resetLink", resetLink);
        context.setVariable("name", name);
        log.info("📬 이메일 전송 대상: {}", toEmail);

        String body = templateEngine.process("mail/reset-password", context);

        MimeMessagePreparator message = mime -> {
            mime.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            mime.setSubject("[내 감정온도] 비밀번호 재설정 링크입니다");
            mime.setText(body, "utf-8", "html");
        };

        mailSender.send(message);
    }
}