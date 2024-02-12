package de.ait.smarttestit.services;

import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.User;
import lombok.NonNull;

import java.util.List;

public interface UsersService {

    User getUserOrThrow(@NonNull final Long userId);

    UserDto addUser(@NonNull final NewUserDto newUserDto);

    List<UserDto> getListUsers();

    UserDto deleteUser(@NonNull final Long userId);

    UserDto updateUser(@NonNull final Long userId, @NonNull final UpdateUserDto updateUser);

    UserDto getUser(@NonNull final Long userId);

    List<Exam> addExamToUser(@NonNull final Long userId, @NonNull final Exam exam);
}
