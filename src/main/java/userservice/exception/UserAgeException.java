package userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "User must be older than the minimum age")
public class UserAgeException extends RuntimeException {
    public UserAgeException(String message) {
        super(message);
    }
}
