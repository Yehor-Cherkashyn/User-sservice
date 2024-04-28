package userservice.service.impl;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import userservice.dto.UserRequestDto;
import userservice.dto.UserUpdateDto;
import userservice.exception.UserAgeException;
import userservice.exception.UserBirthdateException;
import userservice.exception.UserEmptyFieldsException;
import userservice.exception.UserNotFoundException;
import userservice.model.User;
import userservice.repository.UserRepository;
import userservice.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    //region Fields
    private final UserRepository userRepository;
    @Value("${user.min_age}")
    private int minAge;
    //endregion

    @Override
    public User get(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User save(User user) {
        if (user.getFirstname() == null
                || user.getLastname() == null
                || user.getBirthdate() == null
                || user.getEmail() == null) {

            throw new UserEmptyFieldsException("One or more fields are missing: "
                    + "email, firstname, lastname, birthdate");
        }
        checkAge(user);

        return userRepository.save(user);
    }

    @Override
    public User update(User user) {
        if (user.getId() == null) {
            throw new UserNotFoundException("Can't update a null user");
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("User with email: " + email + " not found"));
    }

    @Override
    public List<User> findByBirthDateRange(LocalDate birthDateFrom, LocalDate birthDateTo) {
        if (birthDateFrom != null && birthDateTo != null && birthDateFrom.isBefore(birthDateTo)) {
            return userRepository.findByBirthdateBetween(birthDateFrom, birthDateTo);
        } else {
            throw new UserBirthdateException("All fields are required "
                    + "and birthdate from should be less than birthdate to");
        }
    }

    @Override
    public User updatePartial(Long id, UserUpdateDto requestDto) {
        User user = get(id);

        if (requestDto.getEmail() != null && !requestDto.getEmail().isEmpty())
            user.setEmail(requestDto.getEmail());
        if (requestDto.getFirstname() != null && !requestDto.getFirstname().isEmpty())
            user.setFirstname(requestDto.getFirstname());
        if (requestDto.getLastname() != null && !requestDto.getLastname().isEmpty())
            user.setLastname(requestDto.getLastname());
        if (requestDto.getBirthdate() != null) user.setBirthdate(requestDto.getBirthdate());
        if (requestDto.getAddress() != null && !requestDto.getAddress().isEmpty())
            user.setAddress(requestDto.getAddress());
        if (requestDto.getPhoneNumber() != null)
            user.setPhoneNumber(requestDto.getPhoneNumber());

        checkAge(user);

        return update(user);
    }

    @Override
    public User updateComplete(Long id, UserRequestDto requestDto) {
        User user = get(id);

        user.setEmail(requestDto.getEmail());
        user.setFirstname(requestDto.getFirstname());
        user.setLastname(requestDto.getLastname());
        user.setBirthdate(requestDto.getBirthdate());
        user.setAddress(requestDto.getAddress());
        user.setPhoneNumber(requestDto.getPhoneNumber());

        checkAge(user);

        return update(user);
    }

    private void checkAge(User user) {
        LocalDate currentDate = LocalDate.now();
        LocalDate acceptableAge = currentDate.minusYears(minAge);

        if (user.getBirthdate() != null) {
            if (user.getBirthdate().isAfter(acceptableAge))
                throw new UserAgeException("User must be at least " + minAge + " years old.");
        }
    }
}
