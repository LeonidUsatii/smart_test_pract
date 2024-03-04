package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.smarttestit.dto.exam.NewFinishExamDto;
import de.ait.smarttestit.services.FinishExamService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FinishExamController.class)
@AutoConfigureMockMvc
class FinishExamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FinishExamService finishExamService;

    NewFinishExamDto validFinishExamDto = new NewFinishExamDto(List.of(1, 2, 3), 1L);
    String expectedSuccessMessage = "Thank you, the exam has been successfully completed.";
    String expectedErrorMessage = "Exam not found";

    @Test
    void finishExamShouldReturnSuccessMessage() throws Exception {

        when(finishExamService.finishExam(any(NewFinishExamDto.class))).thenReturn(expectedSuccessMessage);

        mockMvc.perform(put("/api/finishExam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validFinishExamDto)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedSuccessMessage));
    }

    @Test
    void finishExamShouldReturnErrorMessageWhenExamNotFound() throws Exception {

        when(finishExamService.finishExam(any(NewFinishExamDto.class))).thenThrow(new EntityNotFoundException(expectedErrorMessage));

        mockMvc.perform(put("/api/finishExam")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(validFinishExamDto)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityNotFoundException))
                .andExpect(result -> assertEquals(expectedErrorMessage, result.getResolvedException().getMessage()));
    }
}
