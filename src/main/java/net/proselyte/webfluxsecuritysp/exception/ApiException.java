package net.proselyte.webfluxsecuritysp.exception;

import lombok.Getter;

/**
 * Исключение, представляющее ошибку API.
 */
public class ApiException extends RuntimeException {

    /**
     * Код ошибки
     */
    @Getter
    protected String errorCode;

    /**
     * Конструктор с сообщением и кодом ошибки.
     *
     * @param message   Сообщение об ошибке
     * @param errorCode Код ошибки
     */
    public ApiException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
