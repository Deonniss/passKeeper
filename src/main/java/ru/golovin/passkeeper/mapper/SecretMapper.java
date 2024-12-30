package ru.golovin.passkeeper.mapper;

import org.mapstruct.Mapper;
import ru.golovin.passkeeper.dto.SecretDto;
import ru.golovin.passkeeper.entity.Secret;

@Mapper(componentModel = "spring")
public interface SecretMapper extends EntityMapper<SecretDto, Secret> {
}
