package net.proselyte.webfluxsecuritysp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import net.proselyte.webfluxsecuritysp.entity.UserRole;

import java.time.LocalDateTime;

/**
 * DTO для пользователя.
 */
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserDto {
    /**
     * Идентификатор пользователя
     */
    private Long userId;

    /**
     * Имя пользователя
     */
    private String username;

    /**
     * Пароль пользователя (только для записи)
     */
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    /**
     * Роль пользователя
     */
    private UserRole role;

    /**
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Флаг активности пользователя
     */
    private boolean enabled;

    /**
     * Время создания пользователя
     */
    private LocalDateTime created_at;

    /**
     * Время обновления пользователя
     */
    private LocalDateTime updated_at;
}
