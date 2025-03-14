package io.flowstate.api.util;


import io.flowstate.api.exception.RestErrorResponseException;
import org.springframework.security.core.Authentication;

import java.util.UUID;

import static io.flowstate.api.exception.ErrorType.ACCOUNT_UNAVAILABLE;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class AuthenticationUtil {

    public static UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RestErrorResponseException(
                    forStatusAndDetail(UNAUTHORIZED, "User is not authenticated")
                            .withErrorType(ACCOUNT_UNAVAILABLE)
                            .build()
            );
        }

        try {
            return UUID.fromString(authentication.getName());
        } catch (IllegalArgumentException e) {
            // In this case, we're using username as the UUID
            return UUID.nameUUIDFromBytes(authentication.getName().getBytes());
        }
    }
}
