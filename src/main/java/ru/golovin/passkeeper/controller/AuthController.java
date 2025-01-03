package ru.golovin.passkeeper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.golovin.passkeeper.dto.AuthRequest;
import ru.golovin.passkeeper.dto.JwtAuthenticationResponse;
import ru.golovin.passkeeper.service.security.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid AuthRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid AuthRequest request) {
        return authenticationService.signIn(request);
    }

    @PostMapping("/refresh-token")
    public JwtAuthenticationResponse refreshToken(@RequestBody String refreshToken) {
        return authenticationService.refreshAccessToken(refreshToken);
    }
}
