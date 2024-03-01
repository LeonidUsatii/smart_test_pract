package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.User;
import de.ait.smarttestit.models.UserRole;
import de.ait.smarttestit.repositories.UserRepository;
import de.ait.smarttestit.services.UserService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

import static de.ait.smarttestit.dto.user.UserDto.from;

@AllArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto addUser(@NonNull final NewUserDto newUserDto) {

        if (userRepository.existsByEmail(newUserDto.getEmail())) {
            throw new RestException(HttpStatus.CONFLICT,
                    "User with email <" + newUserDto.getEmail() + "> already exists");
        }
        User user = new User(
                newUserDto.getFirstName(),
                newUserDto.getLastName(),
                newUserDto.getEmail(),
                newUserDto.getHashPassword(),
                UserRole.valueOf(newUserDto.getUserRole()),
                newUserDto.getLevelOfUser()
        );
        user = userRepository.save(user);

        return from(user);
    }

    @Override
    public UserDto getUser(@NonNull final Long userId) {

        User user = getUserOrThrow(userId);

        return from(user);
    }

    @Override
    public List<UserDto> getListUsers() {
        return userRepository.findAll().stream()
                .map(UserDto::from)
                .toList();
    }

    @Override
    public UserDto deleteUser(@NonNull final Long userId) {

        User user = getUserOrThrow(userId);
        userRepository.delete(user);

        return from(user);
    }

    @Override
    public UserDto updateUser(@NonNull final Long userId, @NonNull final UpdateUserDto updateUser) {

        User userToUpdate = getUserOrThrow(userId);
        userToUpdate.setFirstName(updateUser.getFirstName());
        userToUpdate.setLastName(updateUser.getLastName());
        userToUpdate.setLevelOfUser(updateUser.getLevelOfUser());

        userToUpdate = userRepository.save(userToUpdate);

        return from(userToUpdate);
    }

    @Override
    public User getUserOrThrow(@NonNull final Long userId) {

        return userRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id <" + userId + "> not found"));
    }

    @Override
    public List<Exam> addExamToUser(@NonNull Long userId, @NonNull Exam exam) {

        User user = getUserOrThrow(userId);

        List<Exam> updatedExams = user.getExams();

        updatedExams.add(exam);

        user.setExams(updatedExams);

        userRepository.save(user);

        return updatedExams;
    }
}
