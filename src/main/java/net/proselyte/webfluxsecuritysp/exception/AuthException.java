package net.proselyte.webfluxsecuritysp.exception;

/**
 * Исключение, связанное с аутентификацией.
 */
public class AuthException extends ApiException {
    /**
     * Конструктор с сообщением и кодом ошибки.
     *
     * @param message   Сообщение об ошибке
     * @param errorCode Код ошибки
     */
    public AuthException(String message, String errorCode) {
        super(message, errorCode);
    }
}
