package com.korovko.auth.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.korovko.auth.config.SecurityConfiguration;
import com.korovko.auth.dto.CreateUserRequest;
import com.korovko.auth.entity.Role;
import com.korovko.auth.security.service.TokenProvider;
import com.korovko.auth.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

@WebMvcTest(UserController.class)
@Import(SecurityConfiguration.class)
@ContextConfiguration(classes = {UserController.class})
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private UserService userService;

  @MockBean
  private TokenProvider tokenProvider;

  @Test
  @WithMockUser(roles = "ADMIN")
  void shouldAllowAdminToCreateUser() throws Exception {
    CreateUserRequest request = new CreateUserRequest();
    request.setUsername("newUser");
    request.setFirstName("First");
    request.setLastName("Last");
    request.setRole(Role.USER);
    request.setPassword("password");

    when(userService.createUser(any(CreateUserRequest.class))).thenReturn(UUID.randomUUID());

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(roles = "USER")
  void shouldForbidNonAdminFromCreatingUser() throws Exception {
    CreateUserRequest request = new CreateUserRequest();

    mockMvc.perform(post("/users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(new ObjectMapper().writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }

}
