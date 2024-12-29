package ru.golovin.passkeeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golovin.passkeeper.entity.Secret;

public interface SecretRepository extends JpaRepository<Secret, Long> {
}
