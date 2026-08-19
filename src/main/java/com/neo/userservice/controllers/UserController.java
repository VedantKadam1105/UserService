package com.neo.userservice.controllers;

import com.neo.userservice.entities.User;
import com.neo.userservice.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> saveUser( @RequestBody User user){

        User savedUser=userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getAllUser());
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or #userId == authentication.principal.claims['userId']")
    public ResponseEntity<User> getUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable String userId,@RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(userId,user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }
}