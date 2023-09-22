package com.korovko.auth.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;

}
