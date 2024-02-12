package de.ait.smarttestit.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("Endpoint /users works:")
@DisplayNameGeneration(value = DisplayNameGenerator.ReplaceUnderscores.class)
@ActiveProfiles("test")
@Transactional
class UsersControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("DELETE /users/{user-id}:")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class DeleteUser {

//        @Test
//        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
//        void testDeleteUserPositive() throws Exception {
//
//            Long existingUserId = 1L;
//
//            mockMvc.perform(delete("/api/users/{user-id}", existingUserId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNoContent());
//        }

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
        void testDeleteUserNegative() throws Exception {

            Long nonExistentUserId = 999L;

            mockMvc.perform(delete("/api/users/{user-id}", nonExistentUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }



    @Nested
    @DisplayName("POST /users:")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class PostUser {

        @Test
        void addAddUserPositive() throws Exception {
            NewUserDto newUser = new NewUserDto();
            newUser.setFirstName("Ivan");
            newUser.setLastName("Ivanov");
            newUser.setEmail("simple@mail.com");
            newUser.setHashPassword("qwerty007!");
            newUser.setUserRole("USER");
            newUser.setLevelOfUser(1);

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newUser)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists());
        }

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
        void addUserNegative_UserAlreadyExists() throws Exception {
            NewUserDto newUser = new NewUserDto();
            newUser.setFirstName("Existing");
            newUser.setLastName("User");
            newUser.setEmail("ivan.testov@example.com");
            newUser.setHashPassword("password");
            newUser.setUserRole("USER");
            newUser.setLevelOfUser(1);

            mockMvc.perform(post("/api/users")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(newUser)))
                    .andExpect(status().isConflict());
        }
    }





//    @Nested
//    @DisplayName("GET /users/{user-id}:")
//    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//    class GetUser {
//
//        @Test
//        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
//        void testGetUserPositive() throws Exception {
//            Long userId = 1L;
//
//            mockMvc.perform(get("/api/users/{user-id}", userId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isOk())
//                    .andExpect(jsonPath("$.id").value(userId))
//                    .andExpect(jsonPath("$.firstName").value("Ivan"))
//                    .andExpect(jsonPath("$.lastName").value("Testov"))
//                    .andExpect(jsonPath("$.email").value("ivan.testov@example.com"));
//        }
//
//        @Test
//        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
//        void testGetUserNegative() throws Exception {
//            Long incorrectUserId = 777L;
//
//            mockMvc.perform(get("/api/users/{user-id}", incorrectUserId)
//                            .contentType(MediaType.APPLICATION_JSON))
//                    .andExpect(status().isNotFound());
//        }
//    }

    @Nested
    @DisplayName("GET /users:")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class GetListUsers {

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
        void testGetListUsersPositive() throws Exception {
            mockMvc.perform(get("/api/users")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$[0].id").isNotEmpty())
                    .andExpect(jsonPath("$[0].firstName").value("Ivan"))
                    .andExpect(jsonPath("$[0].lastName").value("Testov"));
        }

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
        void testGetListUsersNegative() throws Exception {

            Long incorrectUserId = 777L;

            mockMvc.perform(get("/api/users/{user-id}", incorrectUserId)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }


    @Nested
    @DisplayName("UPDATE /users/{user-id}:")
    @DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
    class UpdateUser {

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
        void testUpdateUserPositive() throws Exception {
            UpdateUserDto updateUser = new UpdateUserDto();
            updateUser.setFirstName("UpdatedFirstName");
            updateUser.setLastName("UpdatedLastName");
            updateUser.setEmail("updated.email@example.com");
            updateUser.setUserRole("ADMIN");
            updateUser.setLevelOfUser(2);

            Long userId = 1L;

            mockMvc.perform(put("/api/users/{user-id}", userId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateUser)))
                    .andExpect(status().isAccepted())
                    .andExpect(jsonPath("$.firstName").value(updateUser.getFirstName()));
        }

        @Test
        @Sql(scripts = {"/sql/schema.sql", "/sql/users.sql"})
        void testUpdateUserNegative() throws Exception {
            UpdateUserDto updateUser = new UpdateUserDto();

            Long nonExistentUserId = 999L;

            mockMvc.perform(put("/api/users/{user-id}", nonExistentUserId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(updateUser)))
                    .andExpect(status().isNotFound());
        }
    }




}
