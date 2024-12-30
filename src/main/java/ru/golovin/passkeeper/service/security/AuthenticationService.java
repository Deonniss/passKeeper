package ru.golovin.passkeeper.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovin.passkeeper.dto.JwtAuthenticationResponse;
import ru.golovin.passkeeper.dto.SignInRequest;
import ru.golovin.passkeeper.dto.SignUpRequest;
import ru.golovin.passkeeper.entity.Role;
import ru.golovin.passkeeper.entity.User;
import ru.golovin.passkeeper.service.UserService;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public JwtAuthenticationResponse signUp(SignUpRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .build();
        userService.create(user);
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @Transactional
    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        ));
        User user = userService.getByUsername(request.getUsername());
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new JwtAuthenticationResponse(accessToken, refreshToken);
    }

    @Transactional
    public JwtAuthenticationResponse refreshAccessToken(String refreshToken) {
        Pair<String, String> pair = jwtService.refreshToken(refreshToken);
        return new JwtAuthenticationResponse(pair.getFirst(), pair.getSecond());
    }
}
