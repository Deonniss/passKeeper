package ru.golovin.passkeeper.service.security.aes;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class AesEncryptionService implements EncryptionService {

    private static final String ALGORITHM = "AES";

    private final AesKeyProvider aesKeyProvider;

    @SneakyThrows
    @Override
    public String encrypt(String data) {
        SecretKeySpec keySpec = new SecretKeySpec(aesKeyProvider.getAesKey(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @SneakyThrows
    @Override
    public String decrypt(String encryptedData) {
        SecretKeySpec keySpec = new SecretKeySpec(aesKeyProvider.getAesKey(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }
}
