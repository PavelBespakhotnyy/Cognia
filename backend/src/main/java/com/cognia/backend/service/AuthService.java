package com.cognia.backend.service;

import com.cognia.backend.api.ApiException;
import com.cognia.backend.dto.AuthResponseData;
import com.cognia.backend.dto.LoginRequest;
import com.cognia.backend.dto.RegisterParentRequest;
import com.cognia.backend.dto.RegisterStudentRequest;
import com.cognia.backend.dto.RegisterTutorRequest;
import com.cognia.backend.dto.UserResponse;
import com.cognia.backend.entity.Parent;
import com.cognia.backend.entity.Student;
import com.cognia.backend.entity.Tutor;
import com.cognia.backend.entity.User;
import com.cognia.backend.repository.ParentRepository;
import com.cognia.backend.repository.StudentRepository;
import com.cognia.backend.repository.TutorRepository;
import com.cognia.backend.repository.UserRepository;
import com.cognia.backend.security.JwtService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final TutorRepository tutorRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final long expiresIn;

    public AuthService(
            UserRepository userRepository,
            TutorRepository tutorRepository,
            ParentRepository parentRepository,
            StudentRepository studentRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            @Value("${security.jwt.expiration-ms}") long expiresIn) {
        this.userRepository = userRepository;
        this.tutorRepository = tutorRepository;
        this.parentRepository = parentRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.expiresIn = expiresIn / 1000;
    }

    @Transactional
    public UserResponse registerTutor(RegisterTutorRequest request) {
        User user = createUser(request.email(), request.password(), "TUTOR");

        Tutor tutor = new Tutor();
        tutor.setId(UUID.randomUUID());
        tutor.setUser(user);
        tutor.setFirstName(request.firstName());
        tutor.setLastName(request.lastName());
        tutor.setPhone(request.phone());
        tutor.setBio(request.bio());
        tutorRepository.save(tutor);

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse registerStudent(RegisterStudentRequest request) {
        User user = createUser(request.email(), request.password(), "STUDENT");

        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setUser(user);
        student.setFirstName(request.firstName());
        student.setLastName(request.lastName());
        student.setPhone(request.phone());
        student.setGradeLevel(request.gradeLevel());
        student.setSchoolName(request.schoolName());
        studentRepository.save(student);

        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse registerParent(RegisterParentRequest request) {
        User user = createUser(request.email(), request.password(), "PARENT");

        Parent parent = new Parent();
        parent.setId(UUID.randomUUID());
        parent.setUser(user);
        parent.setFirstName(request.firstName());
        parent.setLastName(request.lastName());
        parent.setPhone(request.phone());
        parent.setPreferredPaymentMethod(request.preferredPaymentMethod());
        parentRepository.save(parent);

        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public AuthResponseData login(LoginRequest request) {
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(this::invalidCredentials);

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw invalidCredentials();
        }

        String token = jwtService.generateToken(user.getId(), user.getRole());
        return new AuthResponseData(token, "Bearer", expiresIn, UserResponse.from(user));
    }

    @Transactional(readOnly = true)
    public UserResponse currentUser(UUID userId) {
        return userRepository.findById(userId)
                .map(UserResponse::from)
                .orElseThrow(() -> new ApiException(
                        HttpStatus.NOT_FOUND, "NOT_FOUND", "User was not found."));
    }

    private User createUser(String email, String rawPassword, String role) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ApiException(
                    HttpStatus.CONFLICT, "CONFLICT", "An account with this email already exists.");
        }

        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email.trim().toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setRole(role);
        return userRepository.saveAndFlush(user);
    }

    private ApiException invalidCredentials() {
        return new ApiException(
                HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Invalid email or password.");
    }
}
