package com.gmail.yauhen2012.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static com.gmail.yauhen2012.service.constant.MailConstant.EMAIL_HEADER;

@Component
public class MailUtil {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String to, String content) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(to);
        msg.setSubject(EMAIL_HEADER);
        msg.setText(content);

        javaMailSender.send(msg);
    }

}
