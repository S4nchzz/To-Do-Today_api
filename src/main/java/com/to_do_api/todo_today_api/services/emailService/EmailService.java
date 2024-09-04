package com.to_do_api.todo_today_api.services.emailService;

import java.io.UnsupportedEncodingException;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String body) {
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setFrom("todotodaynoreply@gmail.com", "To-Do Today Verification Code");
            helper.setSubject(subject);
            helper.setText(body, true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }

        mailSender.send(message);
    }
}
