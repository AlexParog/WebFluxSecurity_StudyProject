package net.proselyte.webfluxsecuritysp.security;

import lombok.RequiredArgsConstructor;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import net.proselyte.webfluxsecuritysp.service.UserService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * Класс для аутентификации пользователя в реактивном стиле.
 */
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    /**
     * Метод для аутентификации пользователя.
     *
     * @param authentication данные аутентификации пользователя
     * @return объект Mono, представляющий результат аутентификации
     */
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new RuntimeException("User disabled")))
                .map(userEntity -> authentication);
    }
}
