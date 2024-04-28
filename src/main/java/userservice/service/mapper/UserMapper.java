package userservice.service.mapper;

import org.springframework.stereotype.Component;
import userservice.dto.UserRequestDto;
import userservice.dto.UserResponseDto;
import userservice.model.User;

@Component
public class UserMapper {
    public UserResponseDto toDto(User model) {
        UserResponseDto responseDto = new UserResponseDto();

        responseDto.setId(model.getId());
        responseDto.setEmail(model.getEmail());
        responseDto.setFirstname(model.getFirstname());
        responseDto.setLastname(model.getLastname());
        responseDto.setBirthdate(model.getBirthdate());
        responseDto.setAddress(model.getAddress());
        responseDto.setPhoneNumber(model.getPhoneNumber());

        return responseDto;
    }

    public User toModel(UserRequestDto requestDto) {
        User user = new User();

        user.setEmail(requestDto.getEmail());
        user.setFirstname(requestDto.getFirstname());
        user.setLastname(requestDto.getLastname());
        user.setBirthdate(requestDto.getBirthdate());
        user.setAddress(requestDto.getAddress());
        user.setPhoneNumber(requestDto.getPhoneNumber());

        return user;
    }
}
