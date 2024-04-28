package userservice.exception;

import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import userservice.dto.ApiError;

@ControllerAdvice
public class GlobalExceptionHandler {
    //region Constants
    private static final String BAD_REQUEST = "Bad Request";
    private static final String NOT_FOUND = "NOT_FOUND";
    private static final String NULL_POINTER = "Null pointer encountered";
    private static final String EXCEPTION = "An error occurred";
    private static final String DELIMITER = ": ";
    //endregion

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e) {
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                NOT_FOUND,
                e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserEmptyFieldsException.class)
    public ResponseEntity<ApiError> handleUserEmptyFieldsException(UserEmptyFieldsException e) {
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAgeException.class)
    public ResponseEntity<ApiError> handleUserAgeException(UserAgeException e) {
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserBirthdateException.class)
    public ResponseEntity<ApiError> handleUserBirthdateException(UserBirthdateException e) {
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + DELIMITER +
                        fieldError.getDefaultMessage())
                .findFirst()
                .orElse(e.getMessage());

        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                BAD_REQUEST,
                errorMessage);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiError> handleNullPointerException(NullPointerException e) {
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                NULL_POINTER,
                e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception e) {
        ApiError apiError = new ApiError(LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                EXCEPTION,
                e.getMessage());

        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
