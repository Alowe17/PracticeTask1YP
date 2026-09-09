package com.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class RsaKeyConfig {
    @Bean
    public RSAPublicKey publicKey (KeyPair keyPair) {
        return (RSAPublicKey) keyPair.getPublic();
    }

    @Bean
    public RSAPrivateKey privateKey (KeyPair keyPair) {
        return (RSAPrivateKey) keyPair.getPrivate();
    }

    @Bean
    public KeyPair keyPair () throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(2048);
        return generator.generateKeyPair();
    }
}