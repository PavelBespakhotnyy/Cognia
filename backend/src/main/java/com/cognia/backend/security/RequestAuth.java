package com.cognia.backend.security;

import com.cognia.backend.api.ApiException;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class RequestAuth {

    private final JwtService jwtService;

    public RequestAuth(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    public JwtService.JwtClaims require(String authorizationHeader) {
        return jwtService.parseBearer(authorizationHeader);
    }

    public JwtService.JwtClaims requireRole(String authorizationHeader, String role) {
        JwtService.JwtClaims claims = require(authorizationHeader);
        if (!role.equals(claims.role())) {
            throw new ApiException(
                    HttpStatus.FORBIDDEN, "FORBIDDEN", "This endpoint requires the " + role + " role.");
        }
        return claims;
    }

    public JwtService.JwtClaims requireAnyRole(String authorizationHeader, String... roles) {
        JwtService.JwtClaims claims = require(authorizationHeader);
        if (!Set.of(roles).contains(claims.role())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", "This role cannot use this endpoint.");
        }
        return claims;
    }
}
