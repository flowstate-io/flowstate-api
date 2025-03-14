package io.flowstate.api.controller;

import io.flowstate.api.dto.authentication.AuthenticationRequestDto;
import io.flowstate.api.dto.authentication.AuthenticationResponseDto;
import io.flowstate.api.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.flowstate.api.model.AuthTokens.REFRESH_TOKEN_COOKIE_NAME;
import static io.flowstate.api.util.CookieUtil.addCookie;
import static io.flowstate.api.util.CookieUtil.removeCookie;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-in")
    public ResponseEntity<AuthenticationResponseDto> authenticate(@Valid @RequestBody final AuthenticationRequestDto requestDto) {
        final var authTokens = authenticationService.authenticate(requestDto.email(), requestDto.password());

        return ResponseEntity.ok()
                .header(SET_COOKIE, addCookie(REFRESH_TOKEN_COOKIE_NAME, authTokens.refreshToken(), authTokens.refreshTokenTtl()).toString())
                .body(new AuthenticationResponseDto(authTokens.accessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) final String refreshToken) {
        final var authTokens = authenticationService.refreshToken(refreshToken);

        return ResponseEntity.ok(new AuthenticationResponseDto(authTokens.accessToken()));
    }

    @PostMapping("/sign-out")
    public ResponseEntity<Void> revokeToken(@CookieValue(REFRESH_TOKEN_COOKIE_NAME) final String refreshToken) {
        authenticationService.revokeRefreshToken(refreshToken);

        return ResponseEntity.noContent()
                .header(SET_COOKIE, removeCookie(REFRESH_TOKEN_COOKIE_NAME).toString())
                .build();
    }
}
