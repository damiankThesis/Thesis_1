package com.praca.komis.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    private final JavaMailSender mailSender;

    @Async
    public void send(String to, String subject, String messageText) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Car Rental & Dealer <cars@engineer.pl>");
        message.setReplyTo("Car Rental & Dealer <cars@engineer.pl>");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(messageText);
        mailSender.send(message);
    }
}
