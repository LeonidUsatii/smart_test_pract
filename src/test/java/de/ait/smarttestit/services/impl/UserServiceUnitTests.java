package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.User;
import de.ait.smarttestit.models.UserRole;
import de.ait.smarttestit.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User CRUD:")
class UserServiceUnitTests {

    private static final Long USER_Id = 1L;
    private static final String FIRST_NAME = "FirstName";
    private static final String LAST_NAME = "LastName";
    private static final String EMAIL = "simple@mail.com";
    private static final String PASSWORD = "password";
    private static final UserRole USER_ROLE = UserRole.USER;
    private static final String ROLE_OF_USER = "USER";
    private static final int LEVEL_OF_USER = 5;
    private static final User USER = new User(USER_Id, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USER_ROLE, LEVEL_OF_USER);
    private static final NewUserDto NEW_USER_DTO = new NewUserDto(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, ROLE_OF_USER, LEVEL_OF_USER);

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl usersServices;

    @Nested
    @DisplayName("get user or trow")
    class GetUserOrThrow {

        @Test
        void getUserOrThrowPositive() {

            when(userRepository.findById(USER_Id)).thenReturn(Optional.of(USER));

            User foundUser = usersServices.getUserOrThrow(USER_Id);

            assertNotNull(foundUser, "User should not be null");
            assertEquals(USER_Id, foundUser.getId(), "User ID should match the requested ID");
        }

        @Test
        void getUserOrThrowNegative() {

            when(userRepository.findById(USER_Id)).thenReturn(Optional.empty());

            RestException thrown = assertThrows(RestException.class, () -> usersServices.getUserOrThrow(USER_Id),
                    "Expected getUserOrThrow to throw, but it didn't");

            assertTrue(thrown.getMessage().contains("User with id <" + USER_Id + "> not found"), "Exception message should contain the correct user ID");
        }
    }

    @Nested
    @DisplayName("add user")
    class AddUser {

        @Test
        void testAddUserPositive() {

            when(userRepository.save(any(User.class))).thenReturn(USER);

            UserDto userDto = usersServices.addUser(NEW_USER_DTO);

            assertEquals(FIRST_NAME, userDto.getFirstName());
            assertEquals(LAST_NAME, userDto.getLastName());
            assertEquals(EMAIL, userDto.getEmail());
            assertEquals(PASSWORD, userDto.getHashPassword());
            assertEquals(USER_ROLE, UserRole.USER);
            assertEquals(LEVEL_OF_USER, userDto.getLevelOfUser());

            verify(userRepository).save(any(User.class));
        }

        @Test
        void testAddUserNegative() {

            when(userRepository.existsByEmail(NEW_USER_DTO.getEmail())).thenReturn(true);

            RestException exception = assertThrows(RestException.class,
                    () -> usersServices.addUser(NEW_USER_DTO));

            assertEquals(HttpStatus.CONFLICT, exception.getStatus());
            assertThat(exception.getMessage().trim(),
                    containsString("User with email <simple@mail.com> already exists"));
        }
    }

    @Nested
    @DisplayName("get list users")
    class GetListUsers {

        @Test
        void testGetListUsersPositive() {

            List<User> users = new ArrayList<>();
            users.add(USER);
            List<UserDto> actual = users.stream()
                    .map(UserDto::from)
                    .collect(Collectors.toList());
            given(userRepository.findAll()).willReturn(users);
            List<UserDto> expected = usersServices.getListUsers();
            assertEquals(expected, actual);
            verify(userRepository).findAll();
        }

        @Test
        void testGetListUsersNegative() {

            List<User> users = new ArrayList<>();
            given(userRepository.findAll()).willReturn(users);
            List<UserDto> expected = usersServices.getListUsers();

            RestException exception = Assertions.assertThrows(RestException.class, () -> {
                if (expected.isEmpty()) {
                    throw new RestException(HttpStatus.NOT_FOUND,
                            "The User list is empty");
                }
            });
            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertThat(exception.getMessage().trim(),
                    containsString("The User list is empty"));
        }
    }

    @Nested
    @DisplayName("get user")
    class GetUser {

        @Test
        void testGetUserPositive() {

            when(userRepository.findById(USER_Id)).thenReturn(Optional.of(USER));

            UserDto actual = UserDto.from(USER);
            UserDto expected = usersServices.getUser(USER_Id);

            assertEquals(expected, actual);
        }

        @Test
        void testGetUserNegative() {

            Long userId = 5L;

            RestException exception = assertThrows(RestException.class,
                    () -> usersServices.getUser(userId));

            assertThat(exception.getMessage().trim(),
                    containsString("User with id <" + userId + "> not found"));
        }
    }

    @Nested
    @DisplayName("update user")
    class updateUser {

        @Test
        void testUpdateUserPositive() {

            User user = new User(2L, FIRST_NAME, LAST_NAME, EMAIL, PASSWORD, USER_ROLE, LEVEL_OF_USER);

            UpdateUserDto updateUser = new UpdateUserDto("updateFirstName", "updateLastName", 7);

            when(userRepository.findById(2L)).thenReturn(Optional.of(user));

            when(userRepository.save(any(User.class))).thenReturn(user);

            UserDto updateUserDto = usersServices.updateUser(2L, updateUser);

            assertEquals("updateFirstName", updateUserDto.getFirstName());
            assertEquals("updateLastName", updateUserDto.getLastName());
            assertEquals(7, updateUserDto.getLevelOfUser());
            assertEquals(user.getEmail(), updateUserDto.getEmail());
            assertEquals(user.getHashPassword(), updateUserDto.getHashPassword());

            verify(userRepository).save(user);
        }

        @Test
        void testUpdateUserNegative() {

            Long userId = 2L;

            UpdateUserDto updateUser = new UpdateUserDto("updateFirstName", "updateLastName", 7);

            RestException exception = assertThrows(RestException.class,
                    () -> usersServices.updateUser(userId, updateUser));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("User with id <" + userId + "> not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("delete user")
    class deleteUser {

        @Test
        void testDeleteUserPositive() {

            when(userRepository.findById(USER_Id)).thenReturn(Optional.of(USER));

            UserDto deleteUser = usersServices.deleteUser(USER_Id);

            assertEquals(USER_Id, deleteUser.getId());
            assertEquals(FIRST_NAME, deleteUser.getFirstName());
            assertEquals(LAST_NAME, deleteUser.getLastName());
            assertEquals(EMAIL, deleteUser.getEmail());
            assertEquals(PASSWORD, deleteUser.getHashPassword());
            assertEquals(ROLE_OF_USER, deleteUser.getUserRole());
            assertEquals(LEVEL_OF_USER, deleteUser.getLevelOfUser());
        }

        @Test
        void testDeleteUserNegative() {

            Long userId = 5L;

            RestException exception = assertThrows(RestException.class,
                    () -> usersServices.deleteUser(userId));

            assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
            assertEquals("User with id <" + userId + "> not found", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("add exam to user")
    class AddExamToUser {

        @Test
        void testAddExamToUserPositive() {

            Long userId = 1L;
            Exam exam = new Exam();
            List<Exam> existingExams = new ArrayList<>();

            User user = mock(User.class);

            when(user.getExams()).thenReturn(existingExams);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

            List<Exam> updatedExams = usersServices.addExamToUser(userId, exam);

            verify(user).setExams(anyList());
            verify(userRepository).save(user);
            assertTrue(updatedExams.contains(exam));
        }

        @Test
        void testAddExamToUserNegative() {

            Long userId = 1L;
            Exam exam = new Exam();

            when(userRepository.findById(userId)).thenReturn(Optional.empty());

            assertThrows(RestException.class, () -> usersServices.addExamToUser(userId, exam));
            verify(userRepository, never()).save(any(User.class));
        }
    }
}
