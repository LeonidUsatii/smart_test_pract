package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.mail.EmailRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    private final String fromAddress;

    @Autowired
    public EmailServiceImpl(JavaMailSender mailSender, @Value("${mail.username}") String fromAddress) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendSimpleMessage(EmailRequestDto emailRequest) {
        if (emailRequest == null ||
                !StringUtils.hasText(emailRequest.to()) ||
                !StringUtils.hasText(emailRequest.subject()) ||
                !StringUtils.hasText(emailRequest.text())) {
            throw new IllegalArgumentException("Invalid email request data");
        }

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(emailRequest.to());
        message.setSubject(emailRequest.subject());
        message.setText(emailRequest.text());
        mailSender.send(message);
    }
}
