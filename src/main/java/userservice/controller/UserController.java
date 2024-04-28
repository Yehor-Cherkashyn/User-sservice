package userservice.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import userservice.dto.UserRequestDto;
import userservice.dto.UserResponseDto;
import userservice.dto.UserUpdateDto;
import userservice.model.User;
import userservice.service.UserService;
import userservice.service.mapper.UserMapper;

@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    //region Fields
    private static final String PATH_VARIABLE_ID = "id";
    private static final String REQUEST_PARAM_FROM = "from";
    private static final String REQUEST_PARAM_TO = "to";
    private static final String REQUEST_PARAM_EMAIL = "email";
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    private final UserService userService;
    private final UserMapper dtoMapper;
    //endregion

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(
            @Validated @RequestBody UserRequestDto requestDto) {
        User user = dtoMapper.toModel(requestDto);
        final UserResponseDto responseDto = dtoMapper.toDto(userService.save(user));

        return new ResponseEntity<>(
                responseDto,
                HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateOneOrMoreUserFields(
            @PathVariable(PATH_VARIABLE_ID) Long id,
            @Validated @RequestBody UserUpdateDto requestDto) {
        User updatedUser = userService.updatePartial(id, requestDto);
        final UserResponseDto responseDto = dtoMapper.toDto(updatedUser);

        return ResponseEntity.ok(responseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Validated @RequestBody UserRequestDto requestDto) {
        User updatedUser = userService.updateComplete(id, requestDto);
        final UserResponseDto responseDto = dtoMapper.toDto(updatedUser);

        return ResponseEntity.ok(responseDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<UserResponseDto>> findByBirthDateRange(
            @Validated @RequestParam(REQUEST_PARAM_FROM)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDate birthdateFrom,
            @Validated @RequestParam(REQUEST_PARAM_TO)
            @DateTimeFormat(pattern = DATE_PATTERN) LocalDate birthdateTo) {
        List<User> users = userService.findByBirthDateRange(birthdateFrom, birthdateTo);
        List<UserResponseDto> responseDtos =
                users.stream().map(dtoMapper::toDto).collect(Collectors.toList());

        return ResponseEntity.ok(responseDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(
            @PathVariable Long id) {
        User user = userService.get(id);
        UserResponseDto responseDto = dtoMapper.toDto(user);

        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<UserResponseDto> findByEmail(
            @RequestParam(REQUEST_PARAM_EMAIL)
            @Validated
            @Email(message = "Email should be valid") String email) {
        User user = userService.findByEmail(email);
        UserResponseDto responseDto = dtoMapper.toDto(user);

        return ResponseEntity.ok(responseDto);
    }
}
