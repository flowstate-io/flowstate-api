package io.flowstate.api.mapper;

import io.flowstate.api.dto.UserProfileDto;
import io.flowstate.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserProfileDto toUserProfileDto(final User user) {
        return new UserProfileDto(user.getEmail(), user.getUsername(), user.getFirstName(), user.getLastName(), user.isEmailVerified());
    }

}
