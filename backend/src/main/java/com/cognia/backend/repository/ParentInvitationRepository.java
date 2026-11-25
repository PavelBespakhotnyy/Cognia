package com.cognia.backend.repository;

import com.cognia.backend.entity.ParentInvitation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentInvitationRepository extends JpaRepository<ParentInvitation, UUID> {

    List<ParentInvitation> findByParentIdOrderByCreatedAtDesc(UUID parentId);

    Optional<ParentInvitation> findByTokenHash(String tokenHash);
}
