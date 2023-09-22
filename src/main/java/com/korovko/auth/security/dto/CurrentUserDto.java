package com.korovko.auth.security.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class CurrentUserDto {

    private UUID id;
    private String username;
    private Set<SimpleGrantedAuthority> authorities;
}
