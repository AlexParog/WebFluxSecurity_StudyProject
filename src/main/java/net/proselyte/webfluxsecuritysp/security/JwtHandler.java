package net.proselyte.webfluxsecuritysp.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import net.proselyte.webfluxsecuritysp.exception.UnauthorizedException;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.Date;

/**
 * Обработчик JWT-токена.
 */
public class JwtHandler {

    /**
     * Секрет для подписи токена.
     */
    private final String secret;

    /**
     * Конструктор класса.
     *
     * @param secret секрет
     */
    public JwtHandler(String secret) {
        this.secret = secret;
    }

    /**
     * Результат проверки токена.
     */
    public static class VerificationResult {
        /**
         * Объект, содержащий информацию из токена.
         */
        public Claims claims;

        /**
         * JWT-токен.
         */
        public String token;

        /**
         * Конструктор класса VerificationResult.
         *
         * @param claims объект с информацией из токена
         * @param token  JWT-токен
         */
        public VerificationResult(Claims claims, String token) {
            this.claims = claims;
            this.token = token;
        }
    }


    /**
     * Проверяет доступ по токену.
     *
     * @param accessToken токен доступа
     * @return Mono с результатом проверки токена
     */
    public Mono<VerificationResult> check(String accessToken) {
        return Mono.just(verify(accessToken))
                .onErrorResume(e -> Mono.error(new UnauthorizedException(e.getMessage())));
    }

    /**
     * Извлекает информацию из JWT-токена.
     *
     * @param token JWT-токен
     * @return объект Claims из токена
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString(secret.getBytes()))
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Проверяет валидность токена.
     *
     * @param token JWT-токен
     * @return объект VerificationResult с результатом проверки токена
     */
    private VerificationResult verify(String token) {
        Claims claims = getClaimsFromToken(token);
        final Date expirationDate = claims.getExpiration();

        if (expirationDate.before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        return new VerificationResult(claims, token);
    }
}
