package io.flowstate.api.service;

import io.flowstate.api.entity.User;
import io.flowstate.api.exception.RestErrorResponseException;
import io.flowstate.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.flowstate.api.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.GONE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUserByUsername(final String username) {
        return userRepository.findByUsername(username).orElseThrow(() ->
                new RestErrorResponseException(forStatusAndDetail(GONE, "The user account has been deleted or inactivated")
                        .withErrorType(ACCOUNT_UNAVAILABLE)
                        .build()
                )
        );
    }
}
