package de.ait.smarttestit.controllers;

import de.ait.smarttestit.repositories.ExamTasksRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Endpoint /exams works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
@Transactional
public class ExamsControllerIntegrationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExamTasksRepository examTaskRepository;

    @Nested
    @DisplayName("POST /exams:")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class PostExam {

        @Test
        @Sql(scripts = {"/sql/clear-db.sql", "/sql/data.sql"})
        void testAddExamPositive() throws Exception {

            mockMvc.perform(post("/api/exams")
                            .param("examScore", "75")
                            .param("testStartTime", "2023-02-03T12:00")
                            .param("examEndTime", "2023-02-03T13:00")
                            .param("examDuration", "110")
                            .param("examStatus", "PLANNED")
                            .param("userId", "1")
                            .param("examTaskId", "3"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists());
        }


        @Test
        @Sql(scripts = {"/sql/clear-db.sql", "/sql/data.sql"})
        void testAddExamNegativeInvalidUserId() throws Exception {

            mockMvc.perform(post("/api/exams")
                            .param("examScore", "70")
                            .param("testStartTime", "2023-02-02T12:00")
                            .param("examEndTime", "2023-02-02T13:00")
                            .param("examDuration", "120")
                            .param("examStatus", "PLANNED")
                            .param("userId", "999")
                            .param("examTaskId", "4"))
                    .andExpect(status().isNotFound());
        }

    }

//    @Nested
//    @DisplayName("GET /exams/{exam-id}:")
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    class GetExam {
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testGetExamPositive() throws Exception {
//            Long examId = 2L;
//
//            mockMvc.perform(get("/api/exams/{exam-id}", examId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(examId))
//                    .andExpect(jsonPath("$.examScore").value("85"))
//                    .andExpect(jsonPath("$.examStartTime").value("2023-01-01T10:00"))
//                    .andExpect(jsonPath("$.examEndTime").value("2023-01-01T11:30"))
//                    .andExpect(jsonPath("$.examDuration").value("90"))
//                    .andExpect(jsonPath("$.examStatus").value("PLANNED"))
//                    .andExpect(jsonPath("$.userId").value("1"))
//                    .andExpect(jsonPath("$.applicantId").isEmpty())
//                    .andExpect(jsonPath("$.examTaskId").value("2"));
//        }
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testGetExamNegative() throws Exception {
//            Long nonExistingExamId = 999L;
//
//            mockMvc.perform(get("/api/exams/{exam-id}", nonExistingExamId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /exams:")
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    class GetListExams {
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testGetListExamsPositive() throws Exception {
//            mockMvc.perform(get("/api/exams")
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$").isArray())
//                    .andExpect(jsonPath("$[0].id").isNotEmpty())
//                    .andExpect(jsonPath("$[0].examScore").value("80"))
//                    .andExpect(jsonPath("$[0].examDuration").value("70"));
//        }
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testGetListExamsNegative() throws Exception {
//
//            Long incorrectExamId = 777L;
//
//            mockMvc.perform(get("/api/exams/{exam-id}", incorrectExamId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("DELETE /exams/{exam-id}:")
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    class DeleteExam {
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testDeleteExamPositive() throws Exception {
//
//            Long existingExamId = 2L;
//
//            mockMvc.perform(delete("/api/exams/{exam-id}", existingExamId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNoContent());
//        }
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testDeleteExamNegative() throws Exception {
//
//            Long nonExistentExamId = 999L;
//
//            mockMvc.perform(delete("/api/exams/{exam-id}", nonExistentExamId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }
//
//    @Nested
//    @DisplayName("UPDATE /exams/{exam-id}:")
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    class UpdateExam {
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testUpdateExamPositive() throws Exception {
//
//            mockMvc.perform(put("/api/exams/1")
//                            .param("examId", "1")
//                            .param("examScore", "100")
//                            .param("testStartTime", "2023-02-02T12:00")
//                            .param("examEndTime", "2023-02-02T13:00")
//                            .param("examDuration", "120")
//                            .param("examStatus", "PLANNED")
//                            .param("userId", "1")
//                            .param("examTaskId", "2"))
//                    .andExpect(status().isAccepted())
//                    .andExpect(jsonPath("$.id").exists());
//
//        }
//
//        @Test
//        @Sql(scripts = {"/sql/data.sql"})
//        void testUpdateExamNegative() throws Exception {
//            mockMvc.perform(put("/api/exams/9999")
//                            .param("examId", "1")
//                            .param("examScore", "100")
//                            .param("testStartTime", "2023-02-02T12:00")
//                            .param("examEndTime", "2023-02-02T13:00")
//                            .param("examDuration", "120")
//                            .param("examStatus", "PLANNED")
//                            .param("userId", "1")
//                            .param("examTaskId", "2"))
//                    .andExpect(status().isNotFound());
//        }
//    }
}
