package net.proselyte.webfluxsecuritysp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Исключение, представляющее ошибку отсутствия авторизации.
 */
@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends ApiException {

    /**
     * Конструктор с сообщением об ошибке.
     *
     * @param message Сообщение об ошибке
     */
    public UnauthorizedException(String message) {
        super(message, "UNAUTHORIZED");
    }
}
