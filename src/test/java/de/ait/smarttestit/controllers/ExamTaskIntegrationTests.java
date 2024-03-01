package de.ait.smarttestit.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Endpoint /examTask works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
public class ExamTaskIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("DELETE /examTask/{examTask-id}:")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class DeleteExamTask {

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"})
        void testDeleteExamTaskPositive() throws Exception {

            Long existingExamTaskId = 3L;

            mockMvc.perform(delete("/api/examTask/{examTask-id}", existingExamTaskId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNoContent());
        }

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"})
        void testDeleteExamNegative() throws Exception {

            Long nonExistentExamId = 999L;

            mockMvc.perform(delete("/api/exams/{exam-id}", nonExistentExamId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
