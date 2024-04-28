package userservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserRequestDto extends BasedUserRequestDto {
    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;
    @NotEmpty(message = "Firstname is required")
    private String firstname;
    @NotEmpty(message = "Lastname is required")
    private String lastname;
}
