package de.ait.smarttestit.controllers;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Endpoint /ExamPreparation:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
class ExamPreparationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Sql(scripts = {"/sql/schema.sql", "/sql/data.sql"})
    @DisplayName("Positive test: return of information about the applicant")
    void shouldReturnApplicantInfoForValidToken() throws Exception {

//        mockMvc.perform(get("/api/examPreparation/qwerty"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.firstName").value("John"))
//                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

//    @Test
//    @DisplayName("Negative test: token not found")
//    void shouldReturnNotFoundForInvalidToken() throws Exception {
//
//        mockMvc.perform(get("/api/examPreparation/qwerty777"))
//                .andExpect(status().isNotFound());
//    }
}
