package ru.golovin.passkeeper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.golovin.passkeeper.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);
}
