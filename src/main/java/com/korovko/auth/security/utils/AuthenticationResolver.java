package com.korovko.auth.security.utils;

import com.korovko.auth.security.dto.CurrentUserDto;
import com.korovko.auth.security.dto.UserPrincipal;
import com.korovko.auth.security.mapper.UserPrincipalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationResolver {

    private final UserPrincipalMapper mapper;

    public CurrentUserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return mapper.toCurrentUser((UserPrincipal) authentication.getPrincipal());
    }
}
