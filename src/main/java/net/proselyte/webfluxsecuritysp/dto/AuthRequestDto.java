package net.proselyte.webfluxsecuritysp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

/**
 * DTO для запроса аутентификации.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthRequestDto {

    /**
     * Имя пользователя
     */
    private String username;

    /**
     * Пароль пользователя
     */
    private String password;
}
