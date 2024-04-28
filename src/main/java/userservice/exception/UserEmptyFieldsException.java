package userservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST,
        reason = "Some required fields are empty")
public class UserEmptyFieldsException extends RuntimeException {
    public UserEmptyFieldsException(String message) {
        super(message);
    }
}
