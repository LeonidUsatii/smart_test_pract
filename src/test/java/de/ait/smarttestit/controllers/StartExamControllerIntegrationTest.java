package de.ait.smarttestit.controllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class StartExamControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = {"/sql/data.sql"})
    @DisplayName("Should return ExamTaskWithTestTypeDto successfully")
    void shouldReturnExamTaskWithTestTypeDto() throws Exception {
        Long examId = 1L;

        mockMvc.perform(put("/api/startExam/{exam-id}", examId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.examId").value(examId))
                .andExpect(jsonPath("$.testTypes[0].name").isNotEmpty())
                .andExpect(jsonPath("$.testTypes[0].questions[0].questionText").isNotEmpty());
    }

    @Test
    @DisplayName("Should return 404 if exam not found")
    void shouldReturn404IfExamNotFound() throws Exception {
        Long examId = 999L;

        mockMvc.perform(put("/api/startExam/{exam-id}", examId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}

