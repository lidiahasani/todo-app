package com.crispy.challenge.todoapp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class TokenService {

    private final JwtEncoder encoder;

    private final JwtDecoder jwtDecoder;

    private final Set<String> revokedTokens = new HashSet<>();

    public TokenService(JwtEncoder encoder, JwtDecoder jwtDecoder) {
        this.encoder = encoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(username)
                .issuedAt(now)
                .expiresAt(now.plus(1, ChronoUnit.HOURS))
                .subject(authentication.getName())
                .build();

        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
            Jwt jwt = jwtDecoder.decode(token);
            Instant expiration = (Instant) jwt.getClaims().get("exp");
        return expiration == null || expiration.isBefore(Instant.now());
    }

    public String extractUsername(String token) {
        return (String) extractClaims(token).get("sub");
    }

    public Map<String, Object> extractClaims(String token) {
        return jwtDecoder.decode(token).getClaims();
    }

    public void revokeToken(String token) {
        revokedTokens.add(token);
    }

    public boolean isTokenRevoked(String token) {
        return revokedTokens.contains(token);
    }

}
