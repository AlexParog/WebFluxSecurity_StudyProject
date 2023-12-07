package net.proselyte.webfluxsecuritysp.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import net.proselyte.webfluxsecuritysp.exception.AuthException;
import net.proselyte.webfluxsecuritysp.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.*;

/**
 * Сервис безопасности для аутентификации пользователей и генерации JWT-токенов.
 */
@Component
@RequiredArgsConstructor
public class SecurityService {

    /**
     * Сервис Пользователя.
     */
    private final UserService userService;
    /**
     * Сервис для кодирования паролей.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Секрет для генерации подписи JWT.
     */
    @Value("${jwt.secret}")
    private String secret;
    /**
     * Время жизни токена в секундах.
     */
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    /**
     * Издатель токена.
     */
    @Value("${jwt.issuer}")
    private String issuer;

    /**
     * Аутентификация пользователя и генерация токена доступа.
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return Mono с деталями токена доступа
     */
    public Mono<TokenDetails> authenticate(String username, String password) {
        return userService.getUserByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new AuthException("Account disabled", "USER_ACCOUNT_DISABLED"));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new AuthException("Invalid password", "INVALID_PASSWORD"));
                    }

                    return Mono.just(generateToken(user).toBuilder()
                            .userId(user.getUserId())
                            .build());
                })
                .switchIfEmpty(Mono.error(new AuthException("Invalid username", "INVALID_USERNAME")));
    }

    /**
     * Генерация токена доступа с указанной датой и дополнительными данными.
     *
     * @param expirationDate дата истечения срока действия токена
     * @param claims         дополнительные данные
     * @param subject        тема токена
     * @return детали сгенерированного токена
     */
    private TokenDetails generateToken(Date expirationDate, Map<String, Object> claims, String subject) {
        Date createdDate = new Date();
        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuer(issuer)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setId(UUID.randomUUID().toString())
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secret.getBytes()))
                .compact();

        return TokenDetails.builder()
                .token(token)
                .issuedAt(createdDate)
                .expireAt(expirationDate)
                .build();
    }

    /**
     * Генерация токена доступа с дополнительными данными и сроком действия, заданным в секундах.
     *
     * @param claims  дополнительные данные
     * @param subject тема токена
     * @return детали сгенерированного токена
     */
    private TokenDetails generateToken(Map<String, Object> claims, String subject) {
        Long expirationTimeInMillis = expirationInSeconds * 1000L;
        Date expirationDate = new Date(new Date().getTime() + expirationTimeInMillis);

        return generateToken(expirationDate, claims, subject);
    }

    /**
     * Генерация токена доступа на основе данных пользователя.
     *
     * @param user пользователь
     * @return детали сгенерированного токена
     */
    private TokenDetails generateToken(UserEntity user) {
        Map<String, Object> claims = new HashMap<>() {{
            put("role", user.getRole());
            put("username", user.getUsername());
        }};
        return generateToken(claims, user.getUserId().toString());
    }
}
