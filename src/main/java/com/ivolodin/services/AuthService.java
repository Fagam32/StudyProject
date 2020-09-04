package com.ivolodin.services;

import com.ivolodin.config.jwt.JwtUtils;
import com.ivolodin.config.service.UserDetailsImpl;
import com.ivolodin.dto.LoginDto;
import com.ivolodin.dto.RegistrationDto;
import com.ivolodin.dto.UserInfoDto;
import com.ivolodin.entities.Role;
import com.ivolodin.entities.User;
import com.ivolodin.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public void register(RegistrationDto registrationDto) {
        if (userRepository.existsByUsername(registrationDto.getUsername()))
            throw new IllegalArgumentException("User with {" + registrationDto.getUsername() + "}name already exists");

        if (userRepository.existsByEmail(registrationDto.getEmail()))
            throw new IllegalArgumentException("User with {" + registrationDto.getEmail() + "}email already exists");

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setBirthDate(registrationDto.getBirthdate());
        user.setName(registrationDto.getName());
        user.setSurname(registrationDto.getSurname());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("User {} registered", user.toString());
    }

    public UserInfoDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        UserInfoDto userInfoDto = new UserInfoDto();

        userInfoDto.setRoles(roles);
        userInfoDto.setJwtToken(token);

        log.info("User {} logged in", userInfoDto.toString());
        return userInfoDto;
    }
}
