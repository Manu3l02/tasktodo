package com.example.tasktodo.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailNotifier {

    private final JavaMailSender mailSender;

    public EmailNotifier(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public boolean sendReminder(String to, String subject, String htmlBody) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(
            		message, 
            		MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, 
            		"UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            helper.setFrom("no-reply@tasktodo.it");      

            mailSender.send(message);
            return true;
        } catch (MessagingException ex) {
        	System.err.println("Errore ivio mail" + ex);
            return false;
        }
    }
}
