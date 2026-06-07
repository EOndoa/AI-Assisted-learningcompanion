package com.etienne.AI_Assisted_learningcompanion.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(
            String toEmail,
            String verificationLink
    ) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(toEmail);

        message.setSubject(
                "Verify Your Account - AI Learning Companion"
        );

        message.setText(
                "Welcome to AI Learning Companion.\n\n" +
                        "Click the link below to verify your account:\n\n" +
                        verificationLink
        );

        mailSender.send(message);
    }
}