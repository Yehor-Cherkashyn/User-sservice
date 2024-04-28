package userservice.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import userservice.dto.UserRequestDto;
import userservice.dto.UserResponseDto;
import userservice.model.User;
import userservice.service.UserService;
import userservice.service.mapper.UserMapper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper dtoMapper;

    User user;
    UserResponseDto responseDto;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstname("John");
        user.setLastname("Doe");
        user.setEmail("john@example.com");
        user.setBirthdate(LocalDate.of(2000, 1, 1));

        responseDto = new UserResponseDto();
        responseDto.setId(1L);
        responseDto.setFirstname("John");
        responseDto.setLastname("Doe");
        responseDto.setEmail("john@example.com");
        responseDto.setBirthdate(LocalDate.of(2000, 1, 1));
    }

    @Test
    void createUserTest() throws Exception {
        when(userService.save(any(User.class))).thenReturn(user);
        when(dtoMapper.toModel(any(UserRequestDto.class))).thenReturn(user);
        when(dtoMapper.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstname\": \"John\", "
                                + "\"lastname\": \"Doe\", "
                                + "\"email\": \"john@example.com\", "
                                + "\"birthdate\": \"01.01.2000\" }"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstname").value("John"));
    }

    @Test
    void updateUserTest() throws Exception {
        when(userService.updateComplete(anyLong(), any(UserRequestDto.class))).thenReturn(user);
        when(dtoMapper.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(put("/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"firstname\": \"John\", "
                                + "\"lastname\": \"Doe\", "
                                + "\"email\": \"john@example.com\", "
                                + "\"birthdate\": \"01.01.2000\" }"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("John"));
    }

    @Test
    void deleteUserTest() throws Exception {
        doNothing().when(userService).delete(anyLong());

        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void findByBirthDateRangeTest() throws Exception {
        List<User> users = Collections.singletonList(user);
        when(userService.findByBirthDateRange(
                any(LocalDate.class),
                any(LocalDate.class)))
                .thenReturn(users);
        when(dtoMapper.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(get("/users/search?from=01.01.1990&to=01.01.2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstname").value("John"));
    }

    @Test
    void findByIdTest() throws Exception {
        when(userService.get(anyLong())).thenReturn(user);
        when(dtoMapper.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("John"));
    }

    @Test
    void findByEmailTest() throws Exception {
        when(userService.findByEmail(anyString())).thenReturn(user);
        when(dtoMapper.toDto(any(User.class))).thenReturn(responseDto);

        mockMvc.perform(get("/users?email=john@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname").value("John"));
    }
}
