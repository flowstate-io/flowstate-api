package io.flowstate.api.controller;

import static io.flowstate.api.testdata.TestUserBuilder.userBuilder;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.GONE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import io.flowstate.api.mapper.UserMapper;
import io.flowstate.api.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.server.ResponseStatusException;

@WebMvcTest(UserProfileController.class)
@Import(UserMapper.class)
class UserProfileControllerTest extends ControllerTest {

    private static final String USERNAME = "testUser";

    @MockBean
    private UserService userService;

    @Test
    @WithMockUser(username = USERNAME)
    void getUserProfile_authenticatedUser_returnsProfile() throws Exception {
        final var user = userBuilder().withVerifiedEmail().build();

        when(userService.getUserByUsername(USERNAME)).thenReturn(user);

        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(user.getUsername()))
                .andExpect(jsonPath("$.email").value(user.getEmail()))
                .andExpect(jsonPath("$.firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.emailVerified").value(true));

        verify(userService).getUserByUsername(USERNAME);
    }

    @Test
    void getUserProfile_unauthenticated_returnsUnauthorized() throws Exception {
        mockMvc.perform(get("/api/user/me")).andExpect(status().isUnauthorized());

        verifyNoInteractions(userService);
    }

    @Test
    @WithMockUser(username = USERNAME)
    void getUserProfile_inactiveUser_returnsGone() throws Exception {
        when(userService.getUserByUsername(anyString())).thenThrow(new ResponseStatusException(GONE, "The user account has been deleted or inactivated"));

        mockMvc.perform(get("/api/user/me"))
                .andExpect(status().isGone())
                .andExpect(jsonPath("$.detail").value("The user account has been deleted or inactivated"));
    }

}
