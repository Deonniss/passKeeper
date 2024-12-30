package ru.golovin.passkeeper.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovin.passkeeper.entity.RefreshToken;
import ru.golovin.passkeeper.entity.User;
import ru.golovin.passkeeper.util.DateUtils;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${token.signing.key}")
    private String jwtSigningKey;

    @Value("${token.access.expiration.min}")
    private int accessTokenExpirationMinutes;

    @Value("${token.refresh.expiration.min}")
    private int refreshTokenExpirationMinutes;

    private final RefreshTokenService refreshTokenService;

    public String generateAccessToken(User user) {
        return generateToken(
                Map.of("id", user.getId(), "type", "access"),
                user,
                DateUtils.getExpirationDate(accessTokenExpirationMinutes)
        );
    }

    @Transactional
    public String generateRefreshToken(User user) {
        Date expiresAt = DateUtils.getExpirationDate(refreshTokenExpirationMinutes);
        String refreshToken = generateToken(
                Map.of(
                        "id", user.getId(),
                        "uuid", UUID.randomUUID(),
                        "type", "refresh"
                ),
                user,
                expiresAt
        );
        refreshTokenService.save(refreshToken, user, DateUtils.convertDateToLocalDateTime(expiresAt));
        return refreshToken;
    }

    @Transactional
    public Pair<String, String> refreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = checkRefreshToken(refreshToken);
        User user = storedRefreshToken.getUser();
        String newAccessToken = generateAccessToken(user);
        String newRefreshToken = generateRefreshToken(user);
        return Pair.of(newAccessToken, newRefreshToken);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        if (!extractAllClaims(token).get("type", String.class).equals("access")) {
            throw new IllegalArgumentException("Invalid type token");
        }
        final String username = extractUserName(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, Date expiresAt) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(expiresAt)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private RefreshToken checkRefreshToken(String refreshToken) {
        RefreshToken storedRefreshToken = refreshTokenService.findByToken(refreshToken)
                .orElseThrow(() -> new NullPointerException("Token is not found"));
        if (storedRefreshToken.getExpiration().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }
        return storedRefreshToken;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
