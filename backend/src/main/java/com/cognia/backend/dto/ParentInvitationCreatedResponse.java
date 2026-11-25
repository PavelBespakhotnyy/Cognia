package com.cognia.backend.dto;

import com.cognia.backend.entity.ParentInvitation;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ParentInvitationCreatedResponse(
        UUID id,
        UUID parentId,
        String email,
        String status,
        OffsetDateTime expiresAt,
        String inviteUrl,
        OffsetDateTime createdAt) {

    public static ParentInvitationCreatedResponse from(ParentInvitation invitation, String inviteUrl) {
        return new ParentInvitationCreatedResponse(
                invitation.getId(),
                invitation.getParent().getId(),
                invitation.getEmail(),
                invitation.getStatus(),
                TimeUtil.utc(invitation.getExpiresAt()),
                inviteUrl,
                TimeUtil.utc(invitation.getCreatedAt()));
    }
}
