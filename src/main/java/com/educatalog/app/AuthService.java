package com.educatalog.app;

<<<<<<< HEAD
import org.springframework.beans.factory.annotation.Value;
=======
>>>>>>> origin/TP2
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class AuthService {

    private final JwtEncoder jwtEncoder;

<<<<<<< HEAD
    @Value("${app.security.admin.username}")
    private String adminUsername;

    @Value("${app.security.admin.password}")
    private String adminPassword;

=======
>>>>>>> origin/TP2
    public AuthService(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    public String login(String username, String password) {
        // Mock authentication
<<<<<<< HEAD
        if (!adminUsername.equals(username) || !adminPassword.equals(password)) {
=======
        if (!"admin".equals(username) || !"password".equals(password)) {
            // Very simple mock check. Practical implementation would use UserDetailsService
            // and PasswordEncoder
            if (!("admin".equals(username))) {
                // Let's allow any password for admin for now as per simple mock req
            }
        }

        if (!"admin".equals(username)) {
>>>>>>> origin/TP2
            throw new IllegalArgumentException("Invalid credentials");
        }

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(username)
                .claim("roles", List.of("ADMIN"))
                .build();

        var jwsHeader = org.springframework.security.oauth2.jwt.JwsHeader
                .with(org.springframework.security.oauth2.jose.jws.MacAlgorithm.HS256).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }
}
