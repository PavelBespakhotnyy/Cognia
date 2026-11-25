package com.cognia.backend.service;

import com.cognia.backend.api.ApiException;
import com.cognia.backend.dto.StudentResponse;
import com.cognia.backend.entity.Tutor;
import com.cognia.backend.repository.StudentRepository;
import com.cognia.backend.repository.TutorRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final TutorRepository tutorRepository;

    public StudentService(StudentRepository studentRepository, TutorRepository tutorRepository) {
        this.studentRepository = studentRepository;
        this.tutorRepository = tutorRepository;
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> listForTutor(UUID userId) {
        Tutor tutor = tutorRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "NOT_FOUND", "Tutor profile was not found."));
        return studentRepository.findByTutorIdOrderByCreatedAtDesc(tutor.getId())
                .stream()
                .map(StudentResponse::from)
                .toList();
    }
}
