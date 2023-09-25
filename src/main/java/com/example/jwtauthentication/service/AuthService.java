package com.example.jwtauthentication.service;

import com.example.jwtauthentication.entities.Role;
import com.example.jwtauthentication.entities.User;
import com.example.jwtauthentication.playload.AuthResponse;
import com.example.jwtauthentication.playload.LoginRequest;
import com.example.jwtauthentication.playload.RegisterRequest;
import com.example.jwtauthentication.repositories.UserRepository;
import com.example.jwtauthentication.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(request.getUserName(),request.getPassword()));
        UserDetails userDetails = userRepository.findByUsername(request.getUserName()).orElseThrow();
        String  token = jwtService.getToken(userDetails);
        return AuthResponse.builder()
                .token(token)
                .build();
    }

    public AuthResponse register(RegisterRequest request){
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstname())
                .lastName(request.getLastname())
                .country(request.getCountry())
                .role(Role.USER)
                .build();

        userRepository.save(user);

        return AuthResponse.builder()
                .token(jwtService.getToken(user))
                .build();
    }
}
