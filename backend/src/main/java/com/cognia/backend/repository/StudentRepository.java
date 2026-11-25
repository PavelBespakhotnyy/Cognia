package com.cognia.backend.repository;

import com.cognia.backend.entity.Student;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, UUID> {

    Optional<Student> findByUserId(UUID userId);

    List<Student> findByTutorIdOrderByCreatedAtDesc(UUID tutorId);
}
