package net.proselyte.webfluxsecuritysp.config;

import lombok.extern.slf4j.Slf4j;
import net.proselyte.webfluxsecuritysp.security.AuthenticationManager;
import net.proselyte.webfluxsecuritysp.security.BearerTokenServerAuthenticationConverter;
import net.proselyte.webfluxsecuritysp.security.JwtHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import reactor.core.publisher.Mono;

/**
 * Класс конфигурации безопасности веб-приложения.
 */
@Slf4j
@Configuration
@EnableReactiveMethodSecurity
public class WebSecurityConfig {

    /**
     * Секрет.
     */
    @Value("${jwt.secret}")
    private String secret;

    /**
     * Массив публичных маршрутов, доступных без аутентификации.
     */
    private final String[] publicRoutes = {"/api/v1/auth/register", "/api/v1/auth/login"};

    /**
     * Фильтр безопасности для веб-приложения.
     *
     * @param http                  объект, представляющий конфигурацию безопасности HTTP
     * @param authenticationManager менеджер аутентификации
     * @return SecurityWebFilterChain объект, представляющий цепочку фильтров безопасности
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         AuthenticationManager authenticationManager) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers(HttpMethod.OPTIONS)
                .permitAll()
                .pathMatchers(publicRoutes)
                .permitAll()
                .anyExchange()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> {
                    log.error("IN securityWebFilterChain - UNAUTHORIZED ERROR: {}", e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
                })
                .accessDeniedHandler((swe, e) -> {
                    log.error("IN securityWebFilterChain - ACCESS DENIED: {}", e.getMessage());
                    return Mono.fromRunnable(() -> swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
                })
                .and()
                .addFilterAt(bearerAuthenticationFilter(authenticationManager), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    /**
     * Создает фильтр аутентификации по Bearer-токену.
     *
     * @param authenticationManager менеджер аутентификации
     * @return AuthenticationWebFilter объект, представляющий фильтр аутентификации
     */
    private AuthenticationWebFilter bearerAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationWebFilter bearerAuthenticationWebFilter = new AuthenticationWebFilter(authenticationManager);
        bearerAuthenticationWebFilter.setServerAuthenticationConverter(
                new BearerTokenServerAuthenticationConverter(new JwtHandler(secret)));
        bearerAuthenticationWebFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/**"));

        return bearerAuthenticationWebFilter;
    }
}
