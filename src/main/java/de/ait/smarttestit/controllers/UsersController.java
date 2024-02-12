package de.ait.smarttestit.controllers;

import de.ait.smarttestit.controllers.api.UsersApi;
import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.services.UsersService;
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

    private final UsersService usersService;

    @Override
    public UserDto addUser(NewUserDto newUser) {
        return usersService.addUser(newUser);
    }

    @Override
    public List<UserDto> getListUsers() {
        return usersService.getListUsers();
    }

    @Override
    public UserDto getUser(Long userId) {
        return usersService.getUser(userId);
    }

    @Override
    public ResponseEntity<Void> deleteUser(@PathVariable("user-id") Long userId) {
        usersService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserDto> updateUser(@NonNull final Long userId, @NonNull final UpdateUserDto updateUser) {
        UserDto updatedUser = usersService.updateUser(userId, updateUser);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(updatedUser);
    }
}
