package ru.golovin.passkeeper.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovin.passkeeper.dto.SecretDto;
import ru.golovin.passkeeper.entity.Secret;
import ru.golovin.passkeeper.mapper.SecretMapper;
import ru.golovin.passkeeper.repository.SecretRepository;
import ru.golovin.passkeeper.service.security.aes.EncryptionService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SecretService {

    private final UserService userService;
    private final SecretMapper secretMapper;
    private final SecretRepository secretRepository;
    private final EncryptionService encryptionService;

    @Transactional(readOnly = true)
    public List<SecretDto> get(Pageable pageable, String serviceName) {
        return secretMapper.toDtos(secretRepository.findAllWithFilters(serviceName, pageable));
    }

    @Transactional
    public SecretDto create(SecretDto secretDto) {
        return secretMapper.toDto(
                secretRepository.save(
                        Secret.builder()
                                .user(userService.getCurrentUser())
                                .serviceName(secretDto.getServiceName())
                                .serviceLogin(secretDto.getServiceLogin())
                                .servicePassword(encryptionService.encrypt(secretDto.getServicePassword()))
                                .build()
                )
        );
    }

    @Transactional
    public SecretDto update(Integer secretId, SecretDto secretDto) {
        Secret secret = secretRepository.findById(Long.valueOf(secretId)).orElseThrow(EntityNotFoundException::new);
        userService.validateUserRelationBySecretId(secret.getUser().getId());
        secret.setServiceName(secretDto.getServiceName());
        secret.setServiceLogin(secretDto.getServiceLogin());
        secret.setServicePassword(secretDto.getServicePassword());
        return secretMapper.toDto(secretRepository.save(secret));
    }

    @Transactional
    public void delete(Integer secretId) {
        Secret secret = secretRepository.findById(Long.valueOf(secretId)).orElseThrow(EntityNotFoundException::new);
        userService.validateUserRelationBySecretId(secret.getUser().getId());
        secretRepository.deleteById(Long.valueOf(secretId));
    }
}
