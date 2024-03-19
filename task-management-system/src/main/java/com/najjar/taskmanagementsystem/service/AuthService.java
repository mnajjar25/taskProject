package com.najjar.taskmanagementsystem.service;

import com.najjar.taskmanagementsystem.dto.LoginDto;
import com.najjar.taskmanagementsystem.dto.RegistrationDto;
import com.najjar.taskmanagementsystem.entity.Role;
import com.najjar.taskmanagementsystem.entity.User;
import com.najjar.taskmanagementsystem.exception.TaskAPIException;
import com.najjar.taskmanagementsystem.repository.RoleRepository;
import com.najjar.taskmanagementsystem.repository.UserRepository;
import com.najjar.taskmanagementsystem.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {
    private AuthenticationManager authenticationManager;
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private JwtTokenProvider jwtTokenProvider;

    public String login(LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(), loginDto.getPassword()
        ));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtTokenProvider.generateToken(authentication);
        log.info("user login successfully");
        return token;
    }

    public String register(RegistrationDto registrationDto) {

        if(userRepository.existsByUsername(registrationDto.getUsername())){
            throw new TaskAPIException(HttpStatus.BAD_REQUEST, "Username is already exists!.");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));

        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("ROLE_USER");
        if(userRole != null){
            roles.add(userRole);
        }
        user.setRoles(roles);

        userRepository.save(user);
        log.info("user registered successfully");

        return "User registered successfully!.";
    }
}
