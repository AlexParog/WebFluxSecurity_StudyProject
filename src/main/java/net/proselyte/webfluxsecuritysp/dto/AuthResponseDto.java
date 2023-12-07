package net.proselyte.webfluxsecuritysp.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO для ответа на запрос аутентификации.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AuthResponseDto {

    /**
     * Идентификатор пользователя
     */
    private Long userId;

    /**
     * JWT-токен для аутентификации
     */
    private String token;

    /**
     * Дата и время создания токена
     */
    private Date issuedAt;

    /**
     * Дата и время истечения срока действия токена
     */
    private Date expiresAt;
}
