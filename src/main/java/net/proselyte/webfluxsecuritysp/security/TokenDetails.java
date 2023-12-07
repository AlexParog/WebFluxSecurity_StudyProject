package net.proselyte.webfluxsecuritysp.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Детали токена доступа.
 */
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TokenDetails {

    /**
     * Идентификатор пользователя, связанный с токеном.
     */
    private Long userId;

    /**
     * JWT-токен доступа.
     */
    private String token;

    /**
     * Дата и время выдачи токена.
     */
    private Date issuedAt;

    /**
     * Дата и время истечения срока действия токена.
     */
    private Date expireAt;
}
