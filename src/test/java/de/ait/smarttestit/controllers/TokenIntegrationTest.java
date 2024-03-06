package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc

@DisplayName("Endpoint /token is works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TokenIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("POST /tokens:")
    class generateToken {

        @Test
        @Sql(scripts = "/sql/data.sql")
        void return_generated_token() throws Exception {
            String requestBody = """
                    {
                        "examTitle": "Exam1",
                        "examDuration": 30,
                        "examTaskDtoList": [
                            {
                                "testTypeId": 1,
                                "questionsLevel": 2,
                                "questionsCount": 10
                            },
                            {
                                "testTypeId": 2,
                                "questionsLevel": 1,
                                "questionsCount": 8
                            }
                        ],
                        "applicantInfo": {
                            "firstName": "Ivan",
                            "lastName": "Ivanov",
                            "email": "ivanov@gmail.com",
                            "address": "Berlin",
                            "phoneNumber": "0123456789"
                        }
                    }
                    """;

            mockMvc.perform(post("/api/tokens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(status().isCreated())
                    .andExpect(content().contentType("text/plain;charset=UTF-8"))
                    .andExpect(content().string(not(equalTo(""))));
        }

        @Test
        void return_bad_request_when_applicant_email_wrong_format() throws Exception {
            String requestBody = """
                    {
                        "examTitle": "Exam1",
                        "examDuration": 30,
                        "examTaskDtoList": [
                            {
                                "testTypeId": 1,
                                "questionsLevel": 2,
                                "questionsCount": 10
                            },
                            {
                                "testTypeId": 2,
                                "questionsLevel": 1,
                                "questionsCount": 8
                            }
                        ],
                        "applicantInfo": {
                            "firstName": "Ivan",
                            "lastName": "Ivanov",
                            "email": "ivanovgmail",
                            "address": "Berlin",
                            "phoneNumber": "0123456789"
                        }
                    }
                    """;
            String tokenJson = objectMapper.writeValueAsString(requestBody);
            mockMvc.perform(post("/api/tokens")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(tokenJson))
                    .andExpect(status().isBadRequest());
        }
    }
}

