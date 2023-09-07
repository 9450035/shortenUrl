package org.neshan.shortenurl.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.neshan.shortenurl.dto.UserDTO;
import org.neshan.shortenurl.dto.UserOutDTO;
import org.neshan.shortenurl.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserOutDTO> register(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.register(userDTO));
    }

    @PostMapping("/login")
    public ResponseEntity<UserOutDTO> login(@Valid @RequestBody UserDTO userDTO) {
        return ResponseEntity.ok(userService.login(userDTO));
    }


}
