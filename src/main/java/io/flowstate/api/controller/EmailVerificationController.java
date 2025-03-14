package io.flowstate.api.controller;

import io.flowstate.api.dto.authentication.AuthenticationResponseDto;
import io.flowstate.api.dto.registration.EmailVerificationRequestDto;
import io.flowstate.api.service.AuthenticationService;
import io.flowstate.api.service.EmailVerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static io.flowstate.api.model.AuthTokens.REFRESH_TOKEN_COOKIE_NAME;
import static io.flowstate.api.util.CookieUtil.addCookie;
import static org.springframework.http.HttpHeaders.SET_COOKIE;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class EmailVerificationController {
    private final EmailVerificationService emailVerificationService;

    private final AuthenticationService authenticationService;

    @PostMapping("/request-verification-email")
    public ResponseEntity<Void> resendVerificationOtp(@RequestParam final String email) {
        emailVerificationService.resendEmailVerificationOtp(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-email")
    public ResponseEntity<AuthenticationResponseDto> verifyOtp(@Valid @RequestBody final EmailVerificationRequestDto requestDto) {
        final var verifiedUser = emailVerificationService.verifyEmailOtp(requestDto.email(), requestDto.otp());
        final var authTokens = authenticationService.authenticate(verifiedUser);

        return ResponseEntity.ok()
                .header(SET_COOKIE, addCookie(REFRESH_TOKEN_COOKIE_NAME, authTokens.refreshToken(), authTokens.refreshTokenTtl()).toString())
                .body(new AuthenticationResponseDto(authTokens.accessToken()));
    }
}
