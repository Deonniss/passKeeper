package ru.golovin.passkeeper.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.golovin.passkeeper.entity.Secret;

import java.util.List;

public interface SecretRepository extends JpaRepository<Secret, Long> {

    @Query("""
            SELECT s
            FROM Secret s
            WHERE (:serviceName IS NULL OR CAST(s.serviceName AS text) LIKE CONCAT(CAST(:serviceName AS text), '%'))
            """)
    List<Secret> findAllWithFilters(String serviceName, Pageable pageable);
}
