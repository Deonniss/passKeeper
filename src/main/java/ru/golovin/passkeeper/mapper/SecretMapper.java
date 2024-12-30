package ru.golovin.passkeeper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import ru.golovin.passkeeper.dto.SecretDto;
import ru.golovin.passkeeper.entity.Secret;
import ru.golovin.passkeeper.service.security.aes.EncryptionService;

@Mapper(componentModel = "spring")
public abstract class SecretMapper implements EntityMapper<SecretDto, Secret> {

    @Autowired
    private EncryptionService encryptionService;

    @Mapping(target = "servicePassword", source = "servicePassword", qualifiedByName = "decryptPassword")
    public abstract SecretDto toDto(Secret entity);

    @Named("decryptPassword")
    protected String decryptPassword(String encryptedPassword) {
        return encryptionService.decrypt(encryptedPassword);
    }
}
