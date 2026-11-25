package com.cognia.backend.repository;

import com.cognia.backend.entity.Lesson;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LessonRepository extends JpaRepository<Lesson, UUID> {

    List<Lesson> findByTutorIdOrderByStartTimeAsc(UUID tutorId);

    List<Lesson> findByStudentIdOrderByStartTimeAsc(UUID studentId);

    List<Lesson> findByStudentParentIdOrderByStartTimeAsc(UUID parentId);
}
