package com.cognia.backend.service;

import com.cognia.backend.api.ApiException;
import com.cognia.backend.dto.CreateLessonRequest;
import com.cognia.backend.dto.LessonDetailResponse;
import com.cognia.backend.dto.LessonResponse;
import com.cognia.backend.entity.Lesson;
import com.cognia.backend.entity.Parent;
import com.cognia.backend.entity.Student;
import com.cognia.backend.entity.Tutor;
import com.cognia.backend.repository.LessonRepository;
import com.cognia.backend.repository.ParentRepository;
import com.cognia.backend.repository.StudentRepository;
import com.cognia.backend.repository.TutorRepository;
import com.cognia.backend.security.JwtService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LessonService {

    private final LessonRepository lessonRepository;
    private final TutorRepository tutorRepository;
    private final StudentRepository studentRepository;
    private final ParentRepository parentRepository;

    public LessonService(
            LessonRepository lessonRepository,
            TutorRepository tutorRepository,
            StudentRepository studentRepository,
            ParentRepository parentRepository) {
        this.lessonRepository = lessonRepository;
        this.tutorRepository = tutorRepository;
        this.studentRepository = studentRepository;
        this.parentRepository = parentRepository;
    }

    @Transactional(readOnly = true)
    public List<LessonResponse> list(
            JwtService.JwtClaims claims, OffsetDateTime from, OffsetDateTime to) {
        validateRange(from, to);

        List<Lesson> lessons = switch (claims.role()) {
            case "TUTOR" -> {
                Tutor tutor = tutorRepository.findByUserId(claims.userId())
                        .orElseThrow(() -> notFound("Tutor profile was not found."));
                yield lessonRepository.findByTutorIdOrderByStartTimeAsc(tutor.getId());
            }
            case "STUDENT" -> {
                Student student = studentRepository.findByUserId(claims.userId())
                        .orElseThrow(() -> notFound("Student profile was not found."));
                yield lessonRepository.findByStudentIdOrderByStartTimeAsc(student.getId());
            }
            case "PARENT" -> {
                Parent parent = parentRepository.findByUserId(claims.userId())
                        .orElseThrow(() -> notFound("Parent profile was not found."));
                yield lessonRepository.findByStudentParentIdOrderByStartTimeAsc(parent.getId());
            }
            default -> throw forbidden("This role cannot list lessons.");
        };

        return lessons.stream()
                .filter(lesson -> from == null || !lesson.getStartTime().isBefore(from))
                .filter(lesson -> to == null || !lesson.getStartTime().isAfter(to))
                .map(LessonResponse::from)
                .toList();
    }

    @Transactional
    public LessonResponse create(UUID userId, CreateLessonRequest request) {
        Tutor tutor = tutorRepository.findByUserId(userId)
                .orElseThrow(() -> notFound("Tutor profile was not found."));

        if (!request.endTime().isAfter(request.startTime())) {
            throw badRequest("endTime must be after startTime.");
        }

        Student student = null;
        if (request.studentId() != null) {
            student = studentRepository.findById(request.studentId())
                    .orElseThrow(() -> notFound("Student was not found."));
            if (student.getTutor() == null || !tutor.getId().equals(student.getTutor().getId())) {
                throw forbidden("Student is not linked to this tutor.");
            }
        }

        Lesson lesson = new Lesson();
        lesson.setId(UUID.randomUUID());
        lesson.setTutor(tutor);
        lesson.setStudent(student);
        lesson.setStartTime(request.startTime());
        lesson.setEndTime(request.endTime());
        lesson.setSubject(request.subject());
        lesson.setPrice(request.price());
        lesson.setStatus("SCHEDULED");
        lesson.setTopic(request.topic());
        lesson.setLessonNotes(request.lessonNotes());
        lesson.setCreatedAt(OffsetDateTime.now());
        lessonRepository.saveAndFlush(lesson);

        return LessonResponse.from(lesson);
    }

    @Transactional(readOnly = true)
    public LessonDetailResponse get(JwtService.JwtClaims claims, UUID lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> notFound("Lesson was not found."));

        switch (claims.role()) {
            case "TUTOR" -> {
                Tutor tutor = tutorRepository.findByUserId(claims.userId())
                        .orElseThrow(() -> notFound("Tutor profile was not found."));
                if (!tutor.getId().equals(lesson.getTutor().getId())) {
                    throw forbidden("This lesson belongs to another tutor.");
                }
            }
            case "STUDENT" -> {
                Student student = studentRepository.findByUserId(claims.userId())
                        .orElseThrow(() -> notFound("Student profile was not found."));
                if (lesson.getStudent() == null || !student.getId().equals(lesson.getStudent().getId())) {
                    throw forbidden("This lesson belongs to another student.");
                }
            }
            case "PARENT" -> {
                Parent parent = parentRepository.findByUserId(claims.userId())
                        .orElseThrow(() -> notFound("Parent profile was not found."));
                Student student = lesson.getStudent();
                if (student == null || student.getParent() == null
                        || !parent.getId().equals(student.getParent().getId())) {
                    throw forbidden("This lesson belongs to another parent.");
                }
            }
            default -> throw forbidden("This role cannot read lessons.");
        }

        return LessonDetailResponse.from(lesson);
    }

    private void validateRange(OffsetDateTime from, OffsetDateTime to) {
        if (from != null && to != null && to.isBefore(from)) {
            throw badRequest("to must not be before from.");
        }
    }

    private ApiException notFound(String message) {
        return new ApiException(HttpStatus.NOT_FOUND, "NOT_FOUND", message);
    }

    private ApiException forbidden(String message) {
        return new ApiException(HttpStatus.FORBIDDEN, "FORBIDDEN", message);
    }

    private ApiException badRequest(String message) {
        return new ApiException(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message);
    }
}
