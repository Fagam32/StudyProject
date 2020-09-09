package com.ivolodin.services;

import com.ivolodin.config.jwt.JwtUtils;
import com.ivolodin.config.service.UserDetailsImpl;
import com.ivolodin.dto.LoginDto;
import com.ivolodin.dto.PrincipalDto;
import com.ivolodin.dto.RegistrationDto;
import com.ivolodin.dto.UserInfoDto;
import com.ivolodin.entities.Role;
import com.ivolodin.entities.User;
import com.ivolodin.repositories.UserRepository;
import com.ivolodin.utils.MapperUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private AuthenticationManager authenticationManager;

    private JwtUtils jwtUtils;

    @Transactional
    public void register(RegistrationDto registrationDto) {
        registrationDto.optimize();
        if (userRepository.existsByUsername(registrationDto.getUsername()))
            throw new IllegalArgumentException("User with {" + registrationDto.getUsername() + "} name already exists");

        if (userRepository.existsByEmail(registrationDto.getEmail()))
            throw new IllegalArgumentException("User with {" + registrationDto.getEmail() + "} email already exists");

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setBirthdate(registrationDto.getBirthdate());
        user.setName(registrationDto.getName());
        user.setSurname(registrationDto.getSurname());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        userRepository.save(user);
        log.info("User {} registered", user.toString());
    }

    public PrincipalDto login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        PrincipalDto principalDto = new PrincipalDto();
        principalDto.setRoles(roles);
        principalDto.setJwtToken(token);

        log.info("User {} logged in", principalDto);
        return principalDto;
    }

    public UserInfoDto getUserInformation(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return MapperUtils.map(user, UserInfoDto.class);
    }

    public void updateUserInformation(UserInfoDto userInfoDto, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        user.setName(userInfoDto.getName());
        user.setSurname(userInfoDto.getSurname());
        user.setBirthdate(userInfoDto.getBirthdate());
        user.setEmail(userInfoDto.getEmail());
        userRepository.save(user);
    }

    public User getUserFromAuthentication(Authentication authentication) {
        String username = ((UserDetailsImpl) authentication.getPrincipal()).getUsername();
        User user = userRepository.findByUsername(username);
        if (user == null)
            throw new EntityNotFoundException("User not found");
        return user;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }
}
