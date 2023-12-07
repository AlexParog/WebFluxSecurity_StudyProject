package net.proselyte.webfluxsecuritysp.mapper;

import net.proselyte.webfluxsecuritysp.dto.UserDto;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * Маппер для преобразования между объектами UserEntity и UserDto.
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     * Метод для отображения UserEntity на UserDto.
     *
     * @param userEntity объект UserEntity
     * @return объект UserDto
     */
    UserDto mapEntity2Dto(UserEntity userEntity);

    /**
     * Метод для отображения UserDto на UserEntity с обратной конфигурацией.
     *
     * @param userDto объект UserDto
     * @return объект UserEntity
     */
    @InheritInverseConfiguration
    UserEntity mapDto2Entity(UserDto userDto);
}
