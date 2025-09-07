package com.uni.ghorgtool.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class EncryptorUtil {

    @Value("${encryption.password}")
    private String password;

    @Value("${encryption.salt}")
    private String salt;

    private TextEncryptor encryptor;

    @PostConstruct
    public void init() {
        encryptor = Encryptors.text(password, salt);
    }

    public String encrypt(String text) {
        return encryptor.encrypt(text);
    }

    public String decrypt(String encryptedText) {
        return encryptor.decrypt(encryptedText);
    }
}
