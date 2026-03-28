package com.tqwc.feastweb.controller;

import com.tqwc.feastweb.service.impl.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Tang
 * @data 2026/3/28 17:46
 */
@RestController
@RequestMapping("/mail")
public class MailController {
    @Autowired
    private MailService mailService;

    @PostMapping("/send")
    public String sendMail(@RequestParam String to) {
        mailService.sendMail(to, "测试邮件", "<h3>你好，这是一封测试邮件</h3>");
        return "发送成功";
    }
}