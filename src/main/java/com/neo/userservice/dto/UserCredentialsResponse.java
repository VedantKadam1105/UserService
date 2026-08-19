package com.neo.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentialsResponse {
    private String userId;
    private String username;
    private String password; // BCrypt hash
    private List<String> roles;
}