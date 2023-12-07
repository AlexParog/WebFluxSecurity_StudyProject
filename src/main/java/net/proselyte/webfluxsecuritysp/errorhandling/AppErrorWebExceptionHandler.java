package net.proselyte.webfluxsecuritysp.errorhandling;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * Пользовательский обработчик веб-ошибок и форматирования ошибок.
 */
@Component
public class AppErrorWebExceptionHandler extends AbstractErrorWebExceptionHandler {

    /**
     * Конструктор класса AppErrorWebExceptionHandler.
     *
     * @param errorAttributes атрибуты ошибки
     * @param applicationContext контекст приложения
     * @param serverCodecConfigurer конфигуратор серверного кодека
     */
    public AppErrorWebExceptionHandler(AppErrorAttributes errorAttributes, ApplicationContext applicationContext, ServerCodecConfigurer serverCodecConfigurer) {
        super(errorAttributes, new WebProperties.Resources(), applicationContext);
        super.setMessageWriters(serverCodecConfigurer.getWriters());
        super.setMessageReaders(serverCodecConfigurer.getReaders());
    }

    /**
     * Метод для создания маршрутизации обработки ошибок.
     *
     * @param errorAttributes атрибуты ошибки
     * @return RouterFunction объект, представляющий маршрут обработки ошибок
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(final ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), request -> {
            var props = getErrorAttributes(request, ErrorAttributeOptions.defaults());

            return ServerResponse.status(Integer.parseInt(props.getOrDefault("status", 500).toString()))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(props.get("errors")));
        });
    }
}
