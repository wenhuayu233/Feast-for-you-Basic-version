package com.tqwc.feastweb.service.impl;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


/**
 * @author Tang
 * @data 2026/3/28 17:33
 */
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String to, String subject, String content) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            helper.setFrom("你的QQ邮箱");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);

            mailSender.send(mimeMessage);
        } catch (Exception e) {
            throw new RuntimeException("邮件发送失败：" + e.getMessage(), e);
        }
    }

    public void sendVerifyCode(String to, String code) {
        String subject = "邮箱验证码";
        String content = "<h3>你的验证码是：" + code + "</h3><p>5分钟内有效，请勿泄露。</p>";
        sendMail(to, subject, content);
    }
}
