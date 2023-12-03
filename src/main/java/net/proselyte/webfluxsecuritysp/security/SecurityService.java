package net.proselyte.webfluxsecuritysp.security;

import lombok.RequiredArgsConstructor;
import net.proselyte.webfluxsecuritysp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class SecurityService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private Integer expirationInSeconds;
    @Value("${jwt.issuer}")
    private String issuer;

    public Mono<TokenDetails> authenticate(String username, String password) {
        return userRepository.findByUsername(username)
                .flatMap(user -> {
                    if (!user.isEnabled()) {
                        return Mono.error(new RuntimeException(""));
                    }

                    if (!passwordEncoder.matches(password, user.getPassword())) {
                        return Mono.error(new RuntimeException(""));
                    }

                    return Mono.just(new TokenDetails());
                })
                .switchIfEmpty(Mono.error(new RuntimeException("")));
    }

    private TokenDetails generateToken() {
        return null;
    }
}
