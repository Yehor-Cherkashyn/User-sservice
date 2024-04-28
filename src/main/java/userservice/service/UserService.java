package userservice.service;

import java.time.LocalDate;
import java.util.List;
import userservice.dto.UserRequestDto;
import userservice.dto.UserUpdateDto;
import userservice.model.User;

public interface UserService {
    User get(Long id);

    List<User> getAll();

    User save(User user);

    User update(User user);

    void delete(Long id);

    User findByEmail(String email);

    List<User> findByBirthDateRange(LocalDate birthDateFrom, LocalDate birthDateTo);

    User updatePartial(Long id, UserUpdateDto requestDto);

    User updateComplete(Long id, UserRequestDto requestDto);
}
