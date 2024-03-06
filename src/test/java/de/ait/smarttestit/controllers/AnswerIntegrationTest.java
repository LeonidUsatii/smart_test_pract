package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.smarttestit.dto.answer.UpdateAnswerDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Endpoint /answers is works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AnswerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /answers:")
    public class addAnswer {

        @Test
        @Sql(scripts = "/sql/data.sql")
        public void return_created_answer() throws Exception {
            mockMvc.perform(post("/api/questions/{question_id}/answers", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"answerText\": \"answer5\",\n" +
                                    "  \"is_correct\": \"true\",\n" +
                                    "  \"question_id\": 1\n" +
                                    "}"))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        @Sql(scripts = {"/sql/data.sql"})
        public void return_existed_answer() throws Exception {
            mockMvc.perform(post("/api/questions/{question_id}/answers", 1)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\n" +
                                    "  \"answerText\": \"answer1\",\n" +
                                    "  \"is_correct\": \"true\",\n" +
                                    "  \"question_id\": 1\n" +
                                    "}"))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("GET answers:")
    public class GetAllAnswers {

        @Test
        @Sql(scripts = "/sql/data.sql")
        public void return_list_of_answers() throws Exception {
            mockMvc.perform(get("/api/questions/answers"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(24)));
        }

        //todo fix it whenn security implimented
        @Disabled
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_401_for_unauthorized() throws Exception {
            mockMvc.perform(get("/api/questions/answers", 4))
                    .andExpect(status().isUnauthorized());
        }

        //todo fix it whenn security implimented
        @Disabled
        @Test
        @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
        public void return_403_for_not_admin() throws Exception {
            mockMvc.perform(get("/api/questions/answers", 4))
                    .andExpect(status().isForbidden());
        }
    }

    @Nested
    @DisplayName("PUT /api/questions/answers/{answer_id}:")
    public class updateAnswer {

        @Test
        @Sql(scripts = "/sql/data.sql")
        public void return_updated_answer() throws Exception {

            UpdateAnswerDto updatedAnswer = new UpdateAnswerDto("answer10", true);
            String updatedJson = objectMapper.writeValueAsString(updatedAnswer);
            mockMvc.perform(put("/api/questions/1/answers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedJson))
                    .andExpect(status().isOk());
        }

        @Test
        @Sql(scripts = "/sql/data.sql")
        public void return_400_for_bad_format_type() throws Exception {
            UpdateAnswerDto updatedAnswer = new UpdateAnswerDto("", false);
            String updatedJson = objectMapper.writeValueAsString(updatedAnswer);
            mockMvc.perform(put("/api/questions/1/answers/1")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(updatedJson))
                    .andExpect(status().isBadRequest());
        }
    }

    @Nested
    @DisplayName("DELETE answers:")
    public class deleteAnswer {

        @Test
        @Sql(scripts = "/sql/data.sql")
        public void return_empty_of_answers() throws Exception {
            mockMvc.perform(delete("/api/questions/1/answers/1"))
                    .andExpect(status().isOk());
        }

        @Test
        @Sql(scripts = "/sql/data.sql")
        public void return_404_by_delete_answer() throws Exception {
            mockMvc.perform(delete("/api/questions/1/answers/111"))
                    .andExpect(status().isNotFound());
        }
    }
}
