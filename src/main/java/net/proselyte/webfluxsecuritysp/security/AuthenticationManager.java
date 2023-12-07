package net.proselyte.webfluxsecuritysp.security;

import lombok.RequiredArgsConstructor;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import net.proselyte.webfluxsecuritysp.service.UserService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserService userService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId())
                .filter(UserEntity::isEnabled)
                .switchIfEmpty(Mono.error(new RuntimeException("User disabled")))
                .map(userEntity -> authentication);
    }
}
