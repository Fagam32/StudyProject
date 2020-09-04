package com.ivolodin.controller;

import com.ivolodin.dto.LoginDto;
import com.ivolodin.dto.RegistrationDto;
import com.ivolodin.dto.UserInfoDto;
import com.ivolodin.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthConfig {

    @Autowired
    private AuthService authService;

    @PostMapping("/singup")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationDto registrationDto){
        authService.register(registrationDto);
        return new ResponseEntity(HttpStatus.OK);
    }
    @PostMapping("/singin")
    public UserInfoDto login(@Valid @RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }
}
