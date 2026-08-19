package com.neo.userservice.controllers;

import com.neo.userservice.dto.UserCredentialsResponse;
import com.neo.userservice.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/users")
public class InternalUserController {

    private final UserRepository userRepository;

    public InternalUserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/by-username/{username}")
    public ResponseEntity<UserCredentialsResponse> getCredentials(@PathVariable String username) {
        return userRepository.findByUsername(username)
                .map(u -> new UserCredentialsResponse(u.getUserId(), u.getUsername(), u.getPassword(), u.getRoles()))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}