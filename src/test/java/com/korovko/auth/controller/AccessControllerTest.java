package com.korovko.auth.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.korovko.auth.config.SecurityConfiguration;
import com.korovko.auth.repository.UserRepository;
import com.korovko.auth.security.service.AuthenticationService;
import com.korovko.auth.security.service.TokenProvider;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(AccessController.class)
@Import(SecurityConfiguration.class)
@ContextConfiguration(classes = {AccessController.class})
class AccessControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AuthenticationService authenticationService;

  @MockBean
  private UserRepository userRepository;

  @MockBean
  private TokenProvider tokenProvider;

  @Test
  @WithMockUser(roles = "SUPER_USER")
  void shouldAllowAccessToSuperUserEndpointForSuperUser() throws Exception {
    mockMvc.perform(get("/access/super"))
        .andExpect(status().isOk())
        .andExpect(content().string("The user has access to the super user endpoint"));
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldDenyAccessToSuperUserEndpointForRegularUser() throws Exception {
    mockMvc.perform(get("/access/super"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldAllowAccessToRegularUserEndpointForRegularUser() throws Exception {
    mockMvc.perform(get("/access/regular"))
        .andExpect(status().isOk())
        .andExpect(content().string("The user has access to the regular user endpoint"));
  }

  @Test
  @WithMockUser(roles = "SUPER_USER")
  void shouldNotAllowAccessToRegularUserEndpointForSuperUser() throws Exception {
    mockMvc.perform(get("/access/regular"))
        .andExpect(status().isForbidden());
  }

  @Test
  @WithAnonymousUser
  void shouldDenyAccessToSuperUserEndpointForAnonymousUser() throws Exception {
    mockMvc.perform(get("/access/super"))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithAnonymousUser
  void shouldDenyAccessToRegularUserEndpointForAnonymousUser() throws Exception {
    mockMvc.perform(get("/access/regular"))
        .andExpect(status().isUnauthorized());
  }

}
