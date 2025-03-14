package io.flowstate.api.mapper;

import io.flowstate.api.dto.registration.RegistrationRequestDto;
import io.flowstate.api.dto.registration.RegistrationResponseDto;
import io.flowstate.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserRegistrationMapper {

    public User toEntity(final RegistrationRequestDto registrationRequestDto) {
        final var user = new User();

        user.setEmail(registrationRequestDto.email());
        user.setUsername(registrationRequestDto.username());
        user.setPassword(registrationRequestDto.password());
        user.setFirstName(registrationRequestDto.firstName());
        user.setLastName(registrationRequestDto.lastName());

        return user;
    }

    public RegistrationResponseDto toResponseDto(final User user) {
        return new RegistrationResponseDto(user.getEmail(), user.getUsername());
    }

}
