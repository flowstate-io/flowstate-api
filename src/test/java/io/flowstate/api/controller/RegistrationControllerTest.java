package io.flowstate.api.controller;

import static io.flowstate.api.exception.ProblemDetailBuilder.forStatusAndDetail;
import static io.flowstate.api.testdata.TestUserBuilder.userBuilder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.flowstate.api.dto.registration.RegistrationRequestDto;
import io.flowstate.api.entity.User;
import io.flowstate.api.exception.RestErrorResponseException;
import io.flowstate.api.mapper.UserRegistrationMapper;
import io.flowstate.api.service.EmailVerificationService;
import io.flowstate.api.service.UserRegistrationService;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;

@WebMvcTest(RegistrationController.class)
@Import(UserRegistrationMapper.class)
class RegistrationControllerTest extends ControllerTest {

    private static final String USERNAME = "testUser";
    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password123";
    private static final String FIRST_NAME = "Test";
    private static final String LAST_NAME = "User";

    @MockBean
    private EmailVerificationService emailVerificationService;

    @MockBean
    private UserRegistrationService userRegistrationService;

    @Autowired
    private RegistrationController registrationController;

    @Test
    void registerUser_validRequest_returnsOk() throws Exception {
        final var user = userBuilder().build();

        when(userRegistrationService.registerUser(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegistrationRequestDto(USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME))))
                .andExpect(status().isOk());

        verify(userRegistrationService).registerUser(any());
        verify(emailVerificationService).sendEmailVerificationOtp(eq(user.getId()), eq(user.getEmail()));
    }

    @Test
    void registerUser_emailOrUsernameExists_returnsConflict() throws Exception {
        final var errors = Map.of("email", List.of("Email is already taken"));
        final var restErrorResponseException = new RestErrorResponseException(forStatusAndDetail(CONFLICT, "Request validation failed").withProperty("errors", errors).build());

        doThrow(restErrorResponseException)
                .when(userRegistrationService)
                .registerUser(any());

        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegistrationRequestDto(USERNAME, EMAIL, PASSWORD, FIRST_NAME, LAST_NAME))))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.detail").value("Request validation failed"));

        verify(userRegistrationService).registerUser(any());
        verifyNoInteractions(emailVerificationService);
    }

    @Test
    void registerUser_invalidEmailFormat_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegistrationRequestDto(USERNAME, "invalid-email", PASSWORD, FIRST_NAME, LAST_NAME))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.email").value("Please provide a valid email address"));
    }

    @Test
    void registerUser_invalidUsernameLength_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegistrationRequestDto("tu", EMAIL, PASSWORD, FIRST_NAME, LAST_NAME))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors.username").value("Username must be between 3 and 20 characters"));
    }

    private RegistrationRequestDto newRegistrationRequestDto(String username, String email, String password, String firstName, String lastName) {
        return new RegistrationRequestDto(username, email, password, firstName, lastName);
    }

}