package com.educatalog.api;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class JwkSetController {

    private final JWKSource<SecurityContext> jwkSource;

    public JwkSetController(JWKSource<SecurityContext> jwkSource) {
        this.jwkSource = jwkSource;
    }

    @GetMapping("/.well-known/jwks.json")
    public Map<String, Object> keys() {
        try {
            return Map.of("keys",
                    jwkSource
                            .get(new com.nimbusds.jose.jwk.JWKSelector(
                                    new com.nimbusds.jose.jwk.JWKMatcher.Builder().build()), null)
                            .stream().map(jwk -> jwk.toJSONObject()).toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
