package ru.golovin.passkeeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golovin.passkeeper.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserIdAndDeviceId(Long userId, String deviceId);

    void deleteByUserIdAndDeviceId(Long userId, String deviceId);

    void deleteAllByUserId(Long userId);
}
