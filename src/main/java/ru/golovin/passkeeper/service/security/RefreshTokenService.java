package ru.golovin.passkeeper.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovin.passkeeper.entity.RefreshToken;
import ru.golovin.passkeeper.entity.User;
import ru.golovin.passkeeper.repository.RefreshTokenRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Transactional
    public RefreshToken save(String token, User user, LocalDateTime expiresAt) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserIdAndDeviceId(user.getId(), "ID")
                .orElse(null);
        if (refreshToken == null) {
            refreshToken = new RefreshToken();
            refreshToken.setUser(user);
            refreshToken.setDeviceId("ID");
        }
        refreshToken.setToken(token);
        refreshToken.setExpiration(expiresAt);
        return refreshTokenRepository.save(refreshToken);
    }
}
