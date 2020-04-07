package com.gmail.yauhen2012.service.util;

import java.lang.invoke.MethodHandles;
import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MailSendingUtil {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    public static void sendFromGMail(String senderMail, String senderPassword, String destinationMail, String mailHeader, String contentOfTheLetter) {

        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", senderMail);
        props.put("mail.smtp.password", senderPassword);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);
        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(senderMail));
            InternetAddress toAddress = new InternetAddress(destinationMail);
            
            message.addRecipient(Message.RecipientType.TO, toAddress);

            message.setSubject(mailHeader);
            message.setText(contentOfTheLetter);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, senderMail, senderPassword);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }
        catch (MessagingException e) {
            logger.error(e.getMessage());
        }
    }
}
