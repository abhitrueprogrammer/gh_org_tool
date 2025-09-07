package com.uni.ghorgtool.controllers;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uni.ghorgtool.dto.request.UpdateGithubTokenRequest;
import com.uni.ghorgtool.models.User;
import com.uni.ghorgtool.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class Me {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<User> me(Authentication authentication) {
        String userEmail = authentication.getName();
        Optional<User> userOpt = userService.findByEmail(userEmail);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(401).build();
        }

        userService.setOrgs(userEmail);

        User updatedUser = userService.findByEmail(userEmail).orElseThrow();

        return new ResponseEntity<User>(updatedUser, HttpStatus.OK);
    }

    @PatchMapping("/me")
    public ResponseEntity<User> updateGithubToken(
            Authentication authentication,
            @RequestBody UpdateGithubTokenRequest request) {
        String userEmail = authentication.getName();
        User updatedUser = userService.updateGithubToken(userEmail, request.getGithubToken());
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}
