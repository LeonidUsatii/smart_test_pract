package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.smarttestit.dto.question.UpdateQuestionDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc

@DisplayName("Endpoint /questions is works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class QuestionIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /questions:")
    class addQuestion {

        @Test
        @Sql(scripts = "/sql/data.sql")
        void return_created_question() throws Exception {
            mockMvc.perform(post("/api/testTypes/{testType_id}/questions", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"questionText\": \"What is b?\",\n" +
                                    "  \"level\": 1,\n" +
                                    "  \"testTypeId\": 1\n" +
                                    "}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        @Sql(scripts = {"/sql/data.sql"})
        void return_existed_question() throws Exception {
            mockMvc.perform(post("/api/testTypes/{testType_id}/questions", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"questionText\": \"What is a?\",\n" +
                                    "  \"level\": 1,\n" +
                                    "  \"testTypeId\": 1\n" +
                                    "}"))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("GET all questions:")
    class GetQuestion {

        @Test
        @Sql(scripts = "/sql/data.sql")
        void return_list_of_questions() throws Exception {
            mockMvc.perform(get("/api/questions", 1))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(6)));
        }

        //todo fix it whenn security implimented
        @Disabled
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
         void return_401_for_unauthorized() throws Exception {
            mockMvc.perform(get("/api/testTypes/{testType_id}/questions", 1))
                    .andExpect(status().isUnauthorized());
        }

        //todo fix it whenn security implimented
        @Disabled
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
         void return_403_for_not_teacher() throws Exception {
            mockMvc.perform(get("/api/questions", 1))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("PUT /questions:")
    class updateQuestion {

        @Test
        @Sql(scripts = "/sql/data.sql")
        void return_updated_question() throws Exception {

            UpdateQuestionDto updatedQuestion = new UpdateQuestionDto("What is w?", 1, 1L);
            String updatedJson = objectMapper.writeValueAsString(updatedQuestion);
            mockMvc.perform(MockMvcRequestBuilders.put("/api/testTypes/1/questions/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedJson))
                    .andExpect(status().isOk());
        }

        @Test
        @Sql(scripts = "/sql/data.sql")
        void return_400_for_bad_format_type() throws Exception {
            UpdateQuestionDto updatedQuestion = new UpdateQuestionDto("", 1, 1L);
            String updatedJson = objectMapper.writeValueAsString(updatedQuestion);
            mockMvc.perform(MockMvcRequestBuilders.put("/api/testTypes/1/questions/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedJson))
                    .andExpect(status().isBadRequest());
        }
    }

//    @Nested
//    @DisplayName("DELETE questions:")
//    class deleteQuestion {
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_empty_test() throws Exception {
//            mockMvc.perform(delete("/api/testTypes/1/questions/1"))
//                    .andExpect(status().isOk());
//        }
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_404_by_delete_question() throws Exception {
//            mockMvc.perform(delete("/api/testTypes/2/questions/111"))
//                    .andExpect(status().isNotFound());
//        }
//    }
}
