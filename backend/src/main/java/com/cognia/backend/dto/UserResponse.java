package com.cognia.backend.dto;

import com.cognia.backend.entity.User;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record UserResponse(UUID id, String email, String role, OffsetDateTime createdAt) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(), user.getEmail(), user.getRole(), TimeUtil.utc(user.getCreatedAt()));
    }
}
