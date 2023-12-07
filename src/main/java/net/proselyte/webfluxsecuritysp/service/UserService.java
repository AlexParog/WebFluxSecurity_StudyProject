package net.proselyte.webfluxsecuritysp.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import net.proselyte.webfluxsecuritysp.entity.UserRole;
import net.proselyte.webfluxsecuritysp.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Сервис для работы с пользователями.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Регистрация пользователя.
     *
     * @param user данные нового пользователя
     * @return объект Mono, представляющий зарегистрированного пользователя
     */
    public Mono<UserEntity> registerUser(UserEntity user) {
        return userRepository.save(
                user.toBuilder()
                        .password(passwordEncoder.encode(user.getPassword()))
                        .role(UserRole.USER)
                        .enabled(true)
                        .created_at(LocalDateTime.now())
                        .updated_at(LocalDateTime.now())
                        .build()
        ).doOnSuccess(u -> {
            log.info("IN registerUser - user: {} created", u);
        });
    }

    /**
     * Получение пользователя по его идентификатору.
     *
     * @param id идентификатор пользователя
     * @return объект Mono, представляющий пользователя
     */
    public Mono<UserEntity> getUserById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Получение пользователя по его имени пользователя.
     *
     * @param username имя пользователя
     * @return объект Mono, представляющий пользователя
     */
    public Mono<UserEntity> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
