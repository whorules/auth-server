package com.korovko.auth.dto;

import com.korovko.auth.entity.Role;
import lombok.Data;

@Data
public class CreateUserRequest {

    private String firstName;
    private String lastName;
    private Role role;
    private String username;
    private String password;

}
