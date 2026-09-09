package com.auth.controller;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.interfaces.RSAPublicKey;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class JwkController {
    private final RSAPublicKey publicKey;

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> jwks() {
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID("auth-service-key")
                .build();

        return new JWKSet(rsaKey).toJSONObject();
    }
}