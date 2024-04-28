package userservice.service.impl;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import userservice.dto.UserRequestDto;
import userservice.dto.UserUpdateDto;
import userservice.exception.UserAgeException;
import userservice.exception.UserBirthdateException;
import userservice.exception.UserEmptyFieldsException;
import userservice.exception.UserNotFoundException;
import userservice.model.User;
import userservice.repository.UserRepository;
import userservice.service.UserService;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserServiceImpl.class)
class UserServiceImplTest {
    @Autowired
    private UserService userService;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void getUserByIdTest() {
        User mockUser = new User();
        mockUser.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        User result = userService.get(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    public void getUserByIdNotFoundTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () ->
                userService.get(1L));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    public void saveUserWithMissingFieldsTest() {
        User newUser = new User();

        Exception exception = assertThrows(UserEmptyFieldsException.class, () ->
                userService.save(newUser));

        assertTrue(exception.getMessage().contains("missing"));
    }

    @Test
    public void saveUserWithValidAgeTest() {
        User newUser = new User();
        newUser.setFirstname("John");
        newUser.setLastname("Doe");
        newUser.setEmail("john@example.com");
        newUser.setBirthdate(LocalDate.now().minusYears(20));

        when(userRepository.save(any(User.class))).thenReturn(newUser);
        User savedUser = userService.save(newUser);
        assertNotNull(savedUser);
        assertEquals("John", savedUser.getFirstname());
    }

    @Test
    public void saveUserWithInvalidAgeTest() {
        User newUser = new User();
        newUser.setFirstname("John");
        newUser.setLastname("Doe");
        newUser.setEmail("john@example.com");
        newUser.setBirthdate(LocalDate.now());

        Exception exception = assertThrows(UserAgeException.class, () ->
                userService.save(newUser));

        assertTrue(exception.getMessage().contains("years old"));
    }

    @Test
    public void deleteUserTest() {
        doNothing().when(userRepository).deleteById(anyLong());
        userService.delete(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    public void findByEmailTest() {
        User mockUser = new User();
        mockUser.setEmail("john@example.com");
        when(userRepository.findByEmail("john@example.com")).thenReturn(Optional.of(mockUser));

        User foundUser = userService.findByEmail("john@example.com");
        assertNotNull(foundUser);
        assertEquals("john@example.com", foundUser.getEmail());
    }

    @Test
    public void findByEmailNotFoundTest() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UserNotFoundException.class, () ->
                userService.findByEmail("notfound@example.com"));

        assertTrue(exception.getMessage().contains("not found"));
    }

    @Test
    public void getAllUsersTest() {
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAll();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void findByBirthDateRangeValidTest() {
        LocalDate from = LocalDate.now().minusYears(30);
        LocalDate to = LocalDate.now().minusYears(20);
        List<User> users = Arrays.asList(new User(), new User());
        when(userRepository.findByBirthdateBetween(from, to)).thenReturn(users);

        List<User> result = userService.findByBirthDateRange(from, to);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void findByBirthDateRangeInvalidTest() {
        LocalDate from = LocalDate.now().minusYears(20);
        LocalDate to = LocalDate.now().minusYears(30);

        Exception exception = assertThrows(UserBirthdateException.class, () ->
                userService.findByBirthDateRange(from, to));

        assertTrue(exception.getMessage().contains("should be less than"));
    }

    @Test
    public void updatePartialValidTest() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstname("Old Name");

        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setFirstname("New Name");

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updatePartial(1L, updateDto);
        assertNotNull(result);
        assertEquals("New Name", result.getFirstname());
    }

    @Test
    public void updateCompleteValidTest() {
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setFirstname("Old Name");

        UserRequestDto requestDto = new UserRequestDto();
        requestDto.setFirstname("New Name");
        requestDto.setLastname("Doe");
        requestDto.setEmail("test@example.com");
        requestDto.setBirthdate(LocalDate.now().minusYears(25));

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        User result = userService.updateComplete(1L, requestDto);
        assertNotNull(result);
        assertEquals("New Name", result.getFirstname());
    }
}
