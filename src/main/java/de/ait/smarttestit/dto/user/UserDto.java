package de.ait.smarttestit.dto.user;

import de.ait.smarttestit.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
public class UserDto extends NewUserDto{

    @Positive
    @Schema(description = "user ID", example = "1")
    private Long id;

    public UserDto(Long id, String firstName, String lastName, String email, String hashPassword, String userRole, int levelOfUser) {
        super( firstName, lastName, email, hashPassword, userRole, levelOfUser);
        this.id = id;
    }

    public static UserDto from(User user) {

        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getHashPassword(),
                user.getUserRole().toString(),
                user.getLevelOfUser()
        );
    }
}
