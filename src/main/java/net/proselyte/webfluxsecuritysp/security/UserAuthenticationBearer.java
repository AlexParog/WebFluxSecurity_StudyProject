package net.proselyte.webfluxsecuritysp.security;

import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Утилитарный класс для создания аутентификации пользователя на основе результатов проверки JWT токена.
 */
public class UserAuthenticationBearer {

    /**
     * Создает аутентификацию пользователя на основе результатов проверки JWT токена.
     *
     * @param verificationResult результат проверки токена доступа
     * @return Mono с деталями аутентификации пользователя
     */
    public static Mono<Authentication> create(JwtHandler.VerificationResult verificationResult) {
        Claims claims = verificationResult.claims;
        String subject = claims.getSubject();

        String role = claims.get("role", String.class);
        String username = claims.get("username", String.class);
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

        Long principalId = Long.parseLong(subject);
        CustomPrincipal principal = new CustomPrincipal(principalId, username);

        return Mono.justOrEmpty(new UsernamePasswordAuthenticationToken(principal, null, authorities));
    }
}
