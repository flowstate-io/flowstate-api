package io.flowstate.api.controller;

import io.flowstate.api.dto.registration.RegistrationRequestDto;
import io.flowstate.api.dto.registration.RegistrationResponseDto;
import io.flowstate.api.mapper.UserRegistrationMapper;
import io.flowstate.api.service.EmailVerificationService;
import io.flowstate.api.service.UserRegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;

    private final EmailVerificationService emailVerificationService;

    private final UserRegistrationMapper userRegistrationMapper;

    @PostMapping("/sign-up")
    public ResponseEntity<RegistrationResponseDto> registerUser(@Valid @RequestBody final RegistrationRequestDto registrationDTO) {
        final var registeredUser = userRegistrationService.registerUser(userRegistrationMapper.toEntity(registrationDTO));

        emailVerificationService.sendEmailVerificationOtp(registeredUser.getId(), registeredUser.getEmail());

        return ResponseEntity.ok(userRegistrationMapper.toResponseDto(registeredUser));
    }
}
