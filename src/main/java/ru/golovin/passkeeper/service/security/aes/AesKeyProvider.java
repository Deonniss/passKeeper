package ru.golovin.passkeeper.service.security.aes;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@Component
public class AesKeyProvider {

    @Value("${encryption.aes.keyfile.path}")
    private String keyFilePath;

    private byte[] aesKey;

    @PostConstruct
    public void init() {
        try {
            aesKey = Files.readAllBytes(Path.of(keyFilePath));
            validateKeyLength(aesKey);
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read AES key file: " + keyFilePath, e);
        }
    }

    private void validateKeyLength(byte[] key) {
        int length = key.length * 8;
        if (length != 128 && length != 192 && length != 256) {
            throw new IllegalArgumentException("Invalid AES key length: " + length + " bits. Must be 128, 192, or 256 bits.");
        }
    }

    public byte[] getAesKey() {
        return aesKey.clone();
    }

    public String getEncodedKey() {
        return Base64.getEncoder().encodeToString(aesKey);
    }
}

