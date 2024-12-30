package ru.golovin.passkeeper.service.security.aes;

public interface EncryptionService {

    String encrypt(String plaintext);

    String decrypt(String ciphertext);
}
