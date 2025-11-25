package com.spendshare.spendshare.user.controller;

import com.spendshare.spendshare.dto.ApiResponse;
import com.spendshare.spendshare.user.dto.UserRegistrationDTO;
import com.spendshare.spendshare.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> userRegister(@Valid @RequestBody UserRegistrationDTO userDto){
        userService.registerUser(userDto);
        return ResponseEntity.ok(new ApiResponse(true, "User registered successfully"));
    }
}
