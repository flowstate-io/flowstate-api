package io.flowstate.api.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static io.flowstate.api.exception.ErrorType.UNAUTHORIZED;
import static io.flowstate.api.exception.ProblemDetailBuilder.forStatus;

@Component
@RequiredArgsConstructor
@Slf4j
public class BearerTokenAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    public void commence(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException authException) throws IOException {
        final var status = HttpStatus.UNAUTHORIZED;

        log.info("{}: {}", status.getReasonPhrase(), authException.getMessage());

        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), forStatus(status).withErrorType(UNAUTHORIZED).build());
    }
}
