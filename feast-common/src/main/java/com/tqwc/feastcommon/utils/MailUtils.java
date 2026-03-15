package com.tqwc.feastcommon.utils;

/**
 * @author 49462
 * @data 2026/3/15 17:40
 */


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;


/**
 * 发邮件工具类 jdk为1.8,目前是项目jdk为17
 */
public final class MailUtils {
    private static final String USER = "494629876@qq.com"; // 发件人的邮箱地址
    private static final String PASSWORD = "123456"; // 授权码
    private static final String HOST = "smtp.qq.com"; // 需要开通pop3/smtp服务

    /**
     * 使用加密的方式,利用 587 端口进行传输邮件,开启ssl
     * @param to    收件人邮箱
     * @param message    邮件正文
     * @param title 邮件标题
     */
    public static void sendMail(String to, String message, String title) {
        try {
//            Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
//            final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
            //设置邮件会话参数
            Properties props = new Properties();
            //邮箱的发送服务器地址
            props.setProperty("mail.smtp.host", HOST);
//            props.setProperty("mail.smtp.socketFactory.class", SSL_FACTORY);
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            //邮箱发送服务器端口,这里设置为 587 端口
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.socketFactory.port", "587");
            props.put("mail.smtp.auth", "true");
            //获取到邮箱会话,利用匿名内部类的方式,将发送者邮箱用户名和密码授权给jvm
            Session session = Session.getDefaultInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USER, PASSWORD);
                }
            });
            //通过会话,得到一个邮件,用于发送
            Message msg = new MimeMessage(session);
            //设置发件人
            msg.setFrom(new InternetAddress(USER));
            //设置收件人,to为收件人,cc为抄送,bcc为密送
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setRecipients(Message.RecipientType.CC, InternetAddress.parse(to, false));
            msg.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(to, false));

            // 设置邮件标题
            msg.setSubject(title);
            //设置发送的日期
            msg.setSentDate(new Date());
            /*//设置邮件消息
            msg.setText(message);*/
            // 设置邮件的内容体
            msg.setContent(message, "text/html;charset=UTF-8");
            //调用Transport的send方法去发送邮件
            Transport.send(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws Exception { // 做测试用
        MailUtils.sendMail("3231230229@qq.com","你好，这是一封测试邮件，无需回复。","测试邮件");
        System.out.println("发送成功");
    }


}
