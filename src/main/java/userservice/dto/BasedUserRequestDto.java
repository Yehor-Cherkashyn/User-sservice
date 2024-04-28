package userservice.dto;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BasedUserRequestDto {
    @Email(message = "Email should be valid")
    private String email;
    private String firstname;
    private String lastname;
    @JsonFormat(pattern = "dd.MM.yyyy")
    @Past(message = "Birthdate must be in the past")
    private LocalDate birthdate;
    private String address;
    @Pattern(regexp = "\\+380\\d{9}",
            message = "Invalid phone number format. Expected format: +380XXXXXXXXX")
    private String phoneNumber;
}
