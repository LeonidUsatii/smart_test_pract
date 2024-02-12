package de.ait.smarttestit.services.impl;

import de.ait.smarttestit.dto.user.NewUserDto;
import de.ait.smarttestit.dto.user.UpdateUserDto;
import de.ait.smarttestit.dto.user.UserDto;
import de.ait.smarttestit.exceptions.RestException;
import de.ait.smarttestit.models.Exam;
import de.ait.smarttestit.models.User;
import de.ait.smarttestit.models.UserRole;
import de.ait.smarttestit.repositories.UsersRepository;
import de.ait.smarttestit.services.UsersService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static de.ait.smarttestit.dto.user.UserDto.from;

@AllArgsConstructor
@Service
@Component
public class UsersServiceImpl implements UsersService {

    private  final UsersRepository usersRepository;

    @Override
    public UserDto addUser(@NonNull final NewUserDto newUserDto) {

        if (usersRepository.existsByEmail(newUserDto.getEmail())) {
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
        user = usersRepository.save(user);

        return from(user);
    }

    @Override
    public UserDto getUser(@NonNull final Long userId) {

        User user = getUserOrThrow(userId);

        return from(user);
    }

    @Override
    public List<UserDto> getListUsers() {
        List<User> users = usersRepository.findAll();
        if (users != null) {
            return users.stream()
                    .map(UserDto::from)
                    .collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public UserDto deleteUser(@NonNull final Long userId) {

        User user = getUserOrThrow(userId);
        usersRepository.delete(user);

        return from(user);
    }

    @Override
    public UserDto updateUser(@NonNull final Long userId, @NonNull final UpdateUserDto updateUser) {

        User user = getUserOrThrow(userId);
        user.setFirstName(updateUser.getFirstName());
        user.setLastName(updateUser.getLastName());
        user.setEmail(updateUser.getEmail());
        user.setUserRole(UserRole.valueOf(updateUser.getUserRole()));
        user.setLevelOfUser(updateUser.getLevelOfUser());

        user = usersRepository.save(user);

        return from(user);
    }

    @Override
    public User getUserOrThrow(@NonNull final Long userId) {

        return usersRepository.findById(userId)
                .orElseThrow(() -> new RestException(HttpStatus.NOT_FOUND, "User with id <" + userId + "> not found"));
    }

    @Override
    public List<Exam> addExamToUser(@NonNull Long userId, @NonNull Exam exam) {

        User user = getUserOrThrow(userId);

        List<Exam> updatedExams = user.getExams();

        updatedExams.add(exam);

        user.setExams(updatedExams);

        usersRepository.save(user);

        return updatedExams;
    }
}
