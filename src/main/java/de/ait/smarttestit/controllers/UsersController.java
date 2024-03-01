package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.UsersApi;
import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.services.UserService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UsersController implements UsersApi {

    private final UserService userService;

    @Override
    public UserDto addUser(NewUserDto newUser) {
        return userService.addUser(newUser);
    }

    @Override
    public List<UserDto> getListUsers() {
        return userService.getListUsers();
    }

    @Override
    public UserDto getUser(Long userId) {
        return userService.getUser(userId);
    }

    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDto> updateUser(@NonNull final Long userId, @NonNull final UpdateUserDto updateUser) {
        UserDto updatedUser = userService.updateUser(userId, updateUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }
}
