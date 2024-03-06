package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.smarttestit.dto.mail.EmailRequestDto;
import de.ait.smarttestit.services.impl.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MailControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmailService emailService;

    private EmailRequestDto validEmailRequest;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        validEmailRequest = new EmailRequestDto("test@example.com", "Test Subject", "Test body");
    }

    @Test
    void whenSendEmailWithValidData_thenReturns200() throws Exception {
        mockMvc.perform(post("/api/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEmailRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Email sent successfully"));
    }

    @Test
    void whenSendEmailWithValidData_thenReturns500() throws Exception {
        doThrow(new RuntimeException("Internal server error")).when(emailService).sendSimpleMessage(any(EmailRequestDto.class));

        mockMvc.perform(post("/api/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEmailRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Error sending email: Internal server error"));
    }

    @Test
    void whenEmailServiceThrows_thenReturns500() throws Exception {
        doThrow(new RuntimeException("Email server not available"))
                .when(emailService).sendSimpleMessage(any(EmailRequestDto.class));

        mockMvc.perform(post("/api/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validEmailRequest)))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Error sending email: Email server not available"));
    }

    @Test
    void whenRequestBodyIsMissing_thenReturns400() throws Exception {
        mockMvc.perform(post("/api/sendEmail")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
