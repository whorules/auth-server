package com.korovko.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korovko.auth.config.SecurityConfiguration;
import com.korovko.auth.repository.UserRepository;
import com.korovko.auth.security.dto.AuthenticationRequest;
import com.korovko.auth.security.service.AuthenticationService;
import com.korovko.auth.security.service.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AuthController.class)
@Import(SecurityConfiguration.class)
@ContextConfiguration(classes = {AuthController.class})
class AuthControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationService authenticationService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private TokenProvider tokenProvider;

  @Test
  void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("user");
    request.setPassword("password");

    when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn("fake-jwt-token");

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.token").value("fake-jwt-token"));
  }

  @Test
  void shouldReturnUnauthorizedWhenLoginFails() throws Exception {
    AuthenticationRequest request = new AuthenticationRequest();
    request.setUsername("user");
    request.setPassword("wrong-password");

    when(authenticationService.authenticate(any(AuthenticationRequest.class)))
        .thenThrow(new BadCredentialsException("Invalid credentials"));

    mockMvc.perform(post("/auth/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }
}
