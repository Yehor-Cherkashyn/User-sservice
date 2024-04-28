package userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "User birthdate should be in past and shouldn't be empty")
public class UserBirthdateException extends RuntimeException {
    public UserBirthdateException(String message) {
        super(message);
    }
}
