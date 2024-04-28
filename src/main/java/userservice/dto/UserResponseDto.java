package userservice.dto;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserResponseDto {
    private Long id;
    private String email;
    private String firstname;
    private String lastname;
    private LocalDate birthdate;
    private String address;
    private String phoneNumber;
}
