package net.proselyte.webfluxsecuritysp.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Класс для кодирования паролей с использованием алгоритма PBKDF2.
 */
@Component
public class PBFDK2Encoder implements PasswordEncoder {

    /**
     * Секрет для кодирования паролей.
     */
    @Value("${jwt.password.encoder.secret}")
    private String secret;

    /**
     * Количество итераций для кодирования паролей.
     */
    @Value("${jwt.password.encoder.iteration}")
    private Integer iteration;

    /**
     * Длина ключа для кодирования паролей.
     */
    @Value("${jwt.password.encoder.keylength}")
    private Integer keyLength;

    /**
     * Константа, определяющая используемый алгоритм.
     */
    private static final String SECRET_KEY_INSTANCE = "PBKDF2WithHmacSHA512";

    /**
     * Кодирует переданный пароль.
     *
     * @param rawPassword исходный пароль
     * @return закодированный пароль
     */
    @Override
    public String encode(CharSequence rawPassword) {
        try {
            byte[] result = SecretKeyFactory.getInstance(SECRET_KEY_INSTANCE)
                    .generateSecret(new PBEKeySpec(rawPassword.toString().toCharArray(),
                            secret.getBytes(), iteration, keyLength))
                    .getEncoded();
            return Base64.getEncoder()
                    .encodeToString(result);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException();
        }
    }

    /**
     * Проверяет, соответствует ли переданный пароль закодированному.
     *
     * @param rawPassword     исходный пароль
     * @param encodedPassword закодированный пароль для сравнения
     * @return true, если пароли совпадают, иначе false
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
