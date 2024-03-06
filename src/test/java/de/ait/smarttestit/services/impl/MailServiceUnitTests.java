package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.mail.EmailRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MailServiceUnitTests {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailServiceImpl emailService;

    private EmailRequestDto validEmailRequest;

    private EmailRequestDto invalidEmailRequest;

    @BeforeEach
    void setUp() {
        validEmailRequest = new EmailRequestDto("recipient@example.com", "Subject", "Message body");
        invalidEmailRequest = new EmailRequestDto("", "", "");
    }

    @Test
    void whenSendSimpleMessageWithValidRequest_thenSuccess() {

        emailService.sendSimpleMessage(validEmailRequest);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendSimpleMessage_withInvalidData_shouldThrowException() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            emailService.sendSimpleMessage(invalidEmailRequest);
        });

        assertEquals("Invalid email request data", exception.getMessage());
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }
}
