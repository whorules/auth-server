package com.korovko.auth.security.service;

import com.korovko.auth.repository.UserRepository;
import com.korovko.auth.security.mapper.UserPrincipalMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserPrincipalMapper mapper;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(mapper::entityToDto)
                .orElseThrow(() -> new UsernameNotFoundException("Cannot find user by username " + username));
    }
}
