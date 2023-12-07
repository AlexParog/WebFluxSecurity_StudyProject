package net.proselyte.webfluxsecuritysp.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

/**
 * Сущность пользователя.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class UserEntity {
    /**
     * Идентификатор пользователя
     */
    @Id
    private Long userId;

    /**
     * Имя пользователя
     */
    private String username;

    /**
     * Пароль пользователя
     */
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

    /**
     * Метод для маскировки пароля в строковом представлении
     */
    @ToString.Include(name = "password")
    private String maskPassword() {
        return "********";
    }

}
