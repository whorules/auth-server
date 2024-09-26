package com.korovko.auth.controller;

import com.korovko.auth.security.dto.AuthenticationRequest;
import com.korovko.auth.security.dto.AuthenticationResponse;
import com.korovko.auth.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> logIn(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            String token = authenticationService.authenticate(authenticationRequest);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null); // Return 401 Unauthorized
        }
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String test() {
        return "ENDPOINT IS VISIBLE";
    }

}
