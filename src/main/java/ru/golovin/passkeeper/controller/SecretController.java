package ru.golovin.passkeeper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.golovin.passkeeper.dto.SecretDto;
import ru.golovin.passkeeper.service.SecretService;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/secret")
@RequiredArgsConstructor
public class SecretController {

    private final SecretService secretService;

    @GetMapping
    public ResponseEntity<List<SecretDto>> get(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "25", required = false) int size,
            @RequestParam(value = "sort", defaultValue = "id", required = false) String sort,
            @RequestParam(value = "direction", defaultValue = "ASC", required = false) String direction,
            @RequestParam(value = "serviceName", required = false) String serviceName
    ) {
        return ResponseEntity.ok(secretService.get(
                PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sort)),
                serviceName
        ));
    }

    @PostMapping
    public ResponseEntity<SecretDto> create(@RequestBody @Valid SecretDto secretDto) {
        return ResponseEntity.ok(secretService.create(secretDto));
    }

    @PutMapping
    public ResponseEntity<SecretDto> update(@RequestBody @Valid SecretDto secretDto) throws AccessDeniedException {
        return ResponseEntity.ok(secretService.update(secretDto));
    }

    @DeleteMapping
    public ResponseEntity<Void> delete(@RequestBody @Valid SecretDto secretDto) throws AccessDeniedException {
        secretService.delete(secretDto);
        return ResponseEntity.ok().build();
    }
}
