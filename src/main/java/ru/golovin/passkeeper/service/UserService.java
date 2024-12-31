package ru.golovin.passkeeper.service;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.golovin.passkeeper.entity.User;
import ru.golovin.passkeeper.exception.UserAlreadyExistException;
import ru.golovin.passkeeper.repository.UserRepository;

import java.nio.file.AccessDeniedException;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    @Transactional
    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UserAlreadyExistException("Пользователь существует");
        }
        return save(user);
    }

    @Transactional(readOnly = true)
    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    @Transactional(readOnly = true)
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @SneakyThrows
    @Transactional(readOnly = true)
    public void validateUserRelationBySecretId(Integer userId) {
        if (!userId.equals(getCurrentUser().getId())) {
            throw new AccessDeniedException("You do not have permission to access this secret");
        }
    }

    private User save(User user) {
        return repository.save(user);
    }
}
