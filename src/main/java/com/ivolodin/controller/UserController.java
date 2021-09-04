package com.ivolodin.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.ivolodin.model.dto.UserInfoDto;
import com.ivolodin.model.dto.View;
import com.ivolodin.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @JsonView(View.Public.class)
    @GetMapping
    public UserInfoDto getUserInformation(Authentication authentication) {
        return userService.getUserInformation(authentication);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @JsonView(View.Private.class)
    @GetMapping(params = {"full"})
    public UserInfoDto getUserInformation(@RequestParam Boolean full, Authentication authentication) {
        return userService.getUserInformation(authentication);
    }

    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN')")
    @PutMapping
    public ResponseEntity<Object> updateUserInformation(@Valid @RequestBody UserInfoDto userInfoDto,
                                                        Authentication authentication,
                                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            HashMap<String, String> body = new HashMap<>();
            body.put("message", bindingResult.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(body);
        }
        userService.updateUserInformation(userInfoDto, authentication);
        return ResponseEntity.ok().build();
    }
}
