package net.proselyte.webfluxsecuritysp.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

/**
 * Класс для преобразования токена Bearer в аутентификацию на сервере.
 */
@RequiredArgsConstructor
public class BearerTokenServerAuthenticationConverter implements ServerAuthenticationConverter {

    /**
     * JwtHandler.
     */
    private final JwtHandler jwtHandler;
    /**
     * Префикс для токена Bearer.
     */
    private static final String BEARER_PREFIX = "Bearer ";
    /**
     * Функция для извлечения значения токена Bearer.
     */
    private static final Function<String, Mono<String>> getBearerValue = authValue ->
            Mono.justOrEmpty(authValue.substring(BEARER_PREFIX.length()));

    /**
     * Преобразует токен Bearer в аутентификацию на сервере.
     *
     * @param exchange объект, представляющий серверный обмен
     * @return объект Mono, представляющий результат аутентификации
     */
    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return extractHeader(exchange)
                .flatMap(getBearerValue)
                .flatMap(jwtHandler::check)
                .flatMap(UserAuthenticationBearer::create);
    }

    /**
     * Извлекает заголовок из запроса.
     *
     * @param exchange объект, представляющий серверный обмен
     * @return объект Mono, представляющий извлеченный заголовок
     */
    private Mono<String> extractHeader(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest()
                .getHeaders()
                .getFirst(HttpHeaders.AUTHORIZATION));
    }
}
