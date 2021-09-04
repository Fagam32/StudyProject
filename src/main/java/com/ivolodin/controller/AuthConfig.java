package com.ivolodin.controller;

import com.ivolodin.model.dto.LoginDto;
import com.ivolodin.model.dto.PrincipalDto;
import com.ivolodin.model.dto.RegistrationDto;
import com.ivolodin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@CrossOrigin
@RestController
@RequestMapping("/auth")
public class AuthConfig {

    @Autowired
    private UserService authService;

    @PostMapping("/singup")
    public ResponseEntity registerUser(@Valid @RequestBody RegistrationDto registrationDto, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            HashMap<String, String> body = new HashMap<>();
            body.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
        }
        authService.register(registrationDto);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/singin")
    public PrincipalDto login(@Valid @RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }
}
