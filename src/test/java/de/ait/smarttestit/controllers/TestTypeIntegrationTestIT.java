package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.smarttestit.dto.test_type.NewTestTypeDto;
import de.ait.smarttestit.dto.test_type.UpdateTestTypeDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Endpoint /testTypes is works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class TestTypeIntegrationTestIT {

//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Nested
//    @DisplayName("POST /testTypes:")
//    class AddTestType {
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_created_testType() throws Exception {
//            NewTestTypeDto testTypeDto = new NewTestTypeDto("newTestType");
//            String content = objectMapper.writeValueAsString(testTypeDto);
//
//            mockMvc.perform(post("/api/testTypes")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(content))
//                    .andExpect(status().isCreated())
//                    .andExpect(jsonPath("$.id").exists())
//                    .andExpect(jsonPath("$.name").value(testTypeDto.name()));
//        }
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_409_for_existed_test() throws Exception {
//            NewTestTypeDto testTypeDto = new NewTestTypeDto("TestType");
//            String content = objectMapper.writeValueAsString(testTypeDto);
//
//            mockMvc.perform(post("/api/testTypes")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(content))
//                    .andExpect(status().isConflict())
//                    .andExpect(content().string("TestType <TestType> already exists"));
//        }
//    }
//
//    @Nested
//    @DisplayName("GET /api/testTypes:")
//    class GetTestTypes {
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_list_of_testTypes() throws Exception {
//            mockMvc.perform(get("/api/testTypes"))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.size()").exists());
//        }
//
//        //todo fix it when security implimented
//        @Disabled
//        @Test
//        void return_401_for_unauthorized() throws Exception {
//            mockMvc.perform(get("/api/testTypes"))
//                    .andExpect(status().isUnauthorized());
//        }
//
//        //todo fix it when security implimented
//        @Disabled
//        @Test
//        void return_403_for_not_user() throws Exception {
//            mockMvc.perform(get("/api/testTypes"))
//                    .andExpect(status().isForbidden());
//        }
//
//    }
//
//    @Nested
//    @DisplayName("PUT /api/testTypes:")
//    class UpdateTest {
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_updated_testType() throws Exception {
//
//            UpdateTestTypeDto updatedTestType = new UpdateTestTypeDto("newTestType");
//            String updatedJson = objectMapper.writeValueAsString(updatedTestType);
//            mockMvc.perform(put("/api/testTypes/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(updatedJson))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.name").exists());
//        }
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_400_for_bad_format_type() throws Exception {
//            UpdateTestTypeDto testTypeDto = new UpdateTestTypeDto("");
//            String invalidJson = objectMapper.writeValueAsString(testTypeDto);
//
//            mockMvc.perform(MockMvcRequestBuilders.put("/api/testTypes/1")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content(invalidJson))
//                    .andExpect(status().isBadRequest());
//        }
//    }
//
//    @Nested
//    @DisplayName("DELETE /testTypes:")
//    class DeleteTest {
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_empty_testType() {
//            try {
//                mockMvc.perform(delete("/api/testTypes/1"))
//                        .andExpect(status().isOk());
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        @Test
//        @Sql(scripts = "/sql/data.sql")
//        void return_404_by_delete_testType() {
//            try {
//                mockMvc.perform(delete("/api/testTypes/4"))
//                        .andExpect(status().isNotFound());
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//    }
}
