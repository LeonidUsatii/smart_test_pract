package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.MailApi;
import de.ait.smarttestit.dto.StandardResponseDto;
import de.ait.smarttestit.dto.mail.EmailRequestDto;
import de.ait.smarttestit.services.impl.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class MailController implements MailApi {

    private final EmailService emailService;

    @Override
    public ResponseEntity<StandardResponseDto> sendEmail(@RequestBody EmailRequestDto emailRequestDto) {
        if (!EmailRequestDto.isValid(emailRequestDto)) {
            return ResponseEntity.badRequest().body(new StandardResponseDto("Invalid email request"));
        }
        try {
            emailService.sendSimpleMessage(emailRequestDto);
            return ResponseEntity.ok(new StandardResponseDto("Email sent successfully"));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new StandardResponseDto("Error sending email: " + e.getMessage()));
        }
    }
}