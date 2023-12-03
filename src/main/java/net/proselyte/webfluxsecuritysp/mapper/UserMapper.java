package net.proselyte.webfluxsecuritysp.mapper;

import net.proselyte.webfluxsecuritysp.dto.UserDto;
import net.proselyte.webfluxsecuritysp.entity.UserEntity;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto mapEntity2Dto(UserEntity userEntity);

    @InheritInverseConfiguration
    UserEntity mapDto2Entity(UserDto userDto);
}
