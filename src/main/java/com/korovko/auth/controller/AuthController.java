package com.korovko.auth.controller;

import com.korovko.auth.security.dto.AuthenticationRequest;
import com.korovko.auth.security.dto.AuthenticationResponse;
import com.korovko.auth.security.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public AuthenticationResponse logIn(@RequestBody AuthenticationRequest authenticationRequest) {
        return new AuthenticationResponse(authenticationService.authenticate(authenticationRequest));
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String test() {
        return "ENDPOINT IS VISIBLE";
    }


}
