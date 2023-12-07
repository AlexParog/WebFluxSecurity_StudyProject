package net.proselyte.webfluxsecuritysp.repository;

import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * Репозиторий для доступа к сущностям пользователей в базе данных.
 */
@Repository
public interface UserRepository extends R2dbcRepository<UserEntity, Long> {

    /**
     * Метод для поиска пользователя по имени пользователя.
     *
     * @param userName имя пользователя
     * @return объект Mono, представляющий пользователя
     */
    Mono<UserEntity> findByUsername(String userName);
}
