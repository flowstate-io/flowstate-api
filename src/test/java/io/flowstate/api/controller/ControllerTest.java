package io.flowstate.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.flowstate.api.config.JwtConfig;
import io.flowstate.api.config.SecurityConfig;
import io.flowstate.api.config.security.BearerTokenAccessDeniedHandler;
import io.flowstate.api.config.security.BearerTokenAuthenticationEntryPoint;
import io.flowstate.api.service.JpaUserDetailsService;
import io.flowstate.api.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@Import({
        SecurityConfig.class,
        JwtConfig.class,
        BearerTokenAuthenticationEntryPoint.class,
        BearerTokenAccessDeniedHandler.class
})
public abstract class ControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JpaUserDetailsService userDetailsService;

}