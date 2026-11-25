package com.cognia.backend.repository;

import com.cognia.backend.entity.Tutor;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutorRepository extends JpaRepository<Tutor, UUID> {

    Optional<Tutor> findByUserId(UUID userId);
}
