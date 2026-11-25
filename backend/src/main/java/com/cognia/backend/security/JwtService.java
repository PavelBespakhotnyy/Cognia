package com.cognia.backend.security;

import com.cognia.backend.api.ApiException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    private static final String ALGORITHM = "HmacSHA256";
    private static final Pattern STRING_CLAIM = Pattern.compile("\"%s\"\\s*:\\s*\"([^\"]+)\"");
    private static final Pattern NUMBER_CLAIM = Pattern.compile("\"%s\"\\s*:\\s*(\\d+)");

    private final SecretKeySpec key;
    private final long expirationMs;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${security.jwt.expiration-ms}") long expirationMs) {
        byte[] decoded = Base64.getDecoder().decode(secret);
        this.key = new SecretKeySpec(decoded, ALGORITHM);
        this.expirationMs = expirationMs;
    }

    public String generateToken(UUID userId, String role) {
        long now = Instant.now().getEpochSecond();
        String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String claims = String.format(
                "{\"sub\":\"%s\",\"role\":\"%s\",\"iat\":%d,\"exp\":%d}",
                userId, role, now, now + expirationMs / 1000);
        String signingInput = header + "." + base64Url(claims);
        return signingInput + "." + base64Url(hmac(signingInput));
    }

    public JwtClaims parseBearer(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw unauthorized();
        }
        return parseAndValidate(authorizationHeader.substring(7).trim());
    }

    public JwtClaims parseAndValidate(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw unauthorized();
        }

        String signingInput = parts[0] + "." + parts[1];
        String expectedSignature = base64Url(hmac(signingInput));
        if (!MessageDigest.isEqual(
                expectedSignature.getBytes(StandardCharsets.UTF_8),
                parts[2].getBytes(StandardCharsets.UTF_8))) {
            throw unauthorized();
        }

        String claimsJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);
        String subject = extractString(claimsJson, "sub");
        String role = extractString(claimsJson, "role");
        long expiresAt = extractNumber(claimsJson, "exp");

        if (Instant.now().getEpochSecond() > expiresAt) {
            throw unauthorized();
        }

        try {
            return new JwtClaims(UUID.fromString(subject), role);
        } catch (IllegalArgumentException ex) {
            throw unauthorized();
        }
    }

    private ApiException unauthorized() {
        return new ApiException(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Authentication is required.");
    }

    private String extractString(String json, String name) {
        Matcher matcher = Pattern.compile(String.format(STRING_CLAIM.pattern(), name)).matcher(json);
        if (!matcher.find()) {
            throw unauthorized();
        }
        return matcher.group(1);
    }

    private long extractNumber(String json, String name) {
        Matcher matcher = Pattern.compile(String.format(NUMBER_CLAIM.pattern(), name)).matcher(json);
        if (!matcher.find()) {
            throw unauthorized();
        }
        return Long.parseLong(matcher.group(1));
    }

    private String base64Url(String value) {
        return Base64.getUrlEncoder().withoutPadding()
                .encodeToString(value.getBytes(StandardCharsets.UTF_8));
    }

    private String base64Url(byte[] value) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(value);
    }

    private byte[] hmac(String value) {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(key);
            return mac.doFinal(value.getBytes(StandardCharsets.UTF_8));
        } catch (GeneralSecurityException ex) {
            throw new IllegalStateException("Cannot sign JWT", ex);
        }
    }

    public record JwtClaims(UUID userId, String role) {

    }
}
