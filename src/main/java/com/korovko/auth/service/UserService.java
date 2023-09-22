package com.korovko.auth.service;

import com.korovko.auth.dto.CreateUserRequest;
import com.korovko.auth.entity.User;
import com.korovko.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UUID createUser(CreateUserRequest createUserRequest) {
        User user = User.builder()
                .id(UUID.randomUUID())
                .firstName(createUserRequest.getFirstName())
                .lastName(createUserRequest.getLastName())
                .role(createUserRequest.getRole())
                .username(createUserRequest.getUsername())
                .password(passwordEncoder.encode(createUserRequest.getPassword()))
                .registeredAt(LocalDateTime.now())
                .build();

        return userRepository.save(user).getId();
    }
}
