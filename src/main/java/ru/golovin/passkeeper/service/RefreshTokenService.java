package ru.golovin.passkeeper.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.golovin.passkeeper.entity.RefreshToken;
import ru.golovin.passkeeper.entity.User;
import ru.golovin.passkeeper.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshToken createToken(User user, String deviceId) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setToken(generateToken());
        refreshToken.setDeviceId(deviceId);
        refreshToken.setExpiration(LocalDateTime.now().plusDays(30));
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public void deleteToken(Long userId, String deviceId) {
        refreshTokenRepository.deleteByUserIdAndDeviceId(userId, deviceId);
    }

    public void deleteAllTokensForUser(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    private String generateToken() {
        return java.util.UUID.randomUUID().toString();
    }
}
