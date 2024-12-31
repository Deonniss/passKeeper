package ru.golovin.passkeeper.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.golovin.passkeeper.dto.SecretDto;
import ru.golovin.passkeeper.service.SecretService;

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

    @PutMapping("/{secretId}")
    public ResponseEntity<SecretDto> update(
            @PathVariable Integer secretId,
            @RequestBody @Valid SecretDto secretDto
    ) {
        return ResponseEntity.ok(secretService.update(secretId, secretDto));
    }

    @DeleteMapping("/{secretId}")
    public ResponseEntity<Void> delete(@PathVariable Integer secretId) {
        secretService.delete(secretId);
        return ResponseEntity.ok().build();
    }
}
