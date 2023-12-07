package net.proselyte.webfluxsecuritysp.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.security.Principal;

/**
 * Пользовательский принцип.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomPrincipal implements Principal {
    /**
     * Идентификатор пользователя.
     */
    private Long id;
    /**
     * Имя пользователя.
     */
    private String name;
}
