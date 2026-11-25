package com.cognia.backend.repository;

import com.cognia.backend.entity.TutorInvitation;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorInvitationRepository extends JpaRepository<TutorInvitation, UUID> {

    List<TutorInvitation> findByTutorIdOrderByCreatedAtDesc(UUID tutorId);

    Optional<TutorInvitation> findByTokenHash(String tokenHash);
}
