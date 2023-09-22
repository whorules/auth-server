package com.korovko.auth.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/access")
public class AccessController {

    @GetMapping("/super")
    @PreAuthorize("hasRole('ROLE_SUPER_USER')")
    public String superUser() {
        return "The user has access to the super user endpoint";
    }

    @GetMapping("/regular")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String regularUser() {
        return "The user has access to the regular user endpoint";
    }

}
