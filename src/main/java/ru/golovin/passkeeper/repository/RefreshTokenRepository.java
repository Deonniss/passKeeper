package ru.golovin.passkeeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golovin.passkeeper.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    Optional<RefreshToken> findByUserIdAndDeviceId(Integer userId, String deviceId);

    void deleteByUserIdAndDeviceId(Integer userId, String deviceId);

    void deleteAllByUserId(Integer userId);
}
