package com.cognia.backend.dto;

import com.cognia.backend.entity.ParentInvitation;
import com.cognia.backend.util.TimeUtil;
import java.time.OffsetDateTime;
import java.util.UUID;

public record ParentInvitationResponse(
        UUID id,
        UUID parentId,
        String email,
        String status,
        OffsetDateTime expiresAt,
        OffsetDateTime acceptedAt,
        UUID acceptedByStudentId,
        OffsetDateTime createdAt) {

    public static ParentInvitationResponse from(ParentInvitation invitation) {
        return new ParentInvitationResponse(
                invitation.getId(),
                invitation.getParent().getId(),
                invitation.getEmail(),
                invitation.getStatus(),
                TimeUtil.utc(invitation.getExpiresAt()),
                TimeUtil.utc(invitation.getAcceptedAt()),
                invitation.getAcceptedByStudent() == null ? null : invitation.getAcceptedByStudent().getId(),
                TimeUtil.utc(invitation.getCreatedAt()));
    }
}
