package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.TestRequest;
import com.AGETIC.Civil_service_competition.dto.TestResponse;
import com.AGETIC.Civil_service_competition.model.Exam;
import com.AGETIC.Civil_service_competition.model.Test;
import com.AGETIC.Civil_service_competition.repository.ExamRepository;
import com.AGETIC.Civil_service_competition.repository.TestRepository;
import com.AGETIC.Civil_service_competition.service.TestService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Transactional
public class TestServiceImpl implements TestService {

    private final TestRepository testRepo;
    private final ExamRepository examRepo;

    public TestServiceImpl(TestRepository testRepo, ExamRepository examRepo) {
        this.testRepo = testRepo;
        this.examRepo = examRepo;
    }

    @Override
    public List<TestResponse> getAllTests() {
        return testRepo.findAll()
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public TestResponse getTestById(Long id) {
        Test t = testRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found"));
        return toResponse(t);
    }

    @Override
    public TestResponse addTest(TestRequest request) {
        Long examId = request.examId();
        String title = safe(request.title());

        if (examId == null) throw new RuntimeException("examId is required");
        if (title == null || title.isBlank()) throw new RuntimeException("title is required");

        Exam exam = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (testRepo.existsByExamIdAndTitleIgnoreCase(examId, title)) {
            throw new RuntimeException("A test with this title already exists for the exam");
        }

        Test t = new Test();
        t.setExam(exam);
        t.setTitle(title);
        testRepo.save(t);

        return toResponse(t);
    }

    @Override
    public TestResponse updateTest(Long id, TestRequest request) {
        Test t = testRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Test not found"));

        // exam change (optional)
        if (request.examId() != null && !Objects.equals(t.getExam().getId(), request.examId())) {
            Exam exam = examRepo.findById(request.examId())
                    .orElseThrow(() -> new RuntimeException("Exam not found"));
            t.setExam(exam);
        }

        // title change
        if (request.title() != null) {
            String title = safe(request.title());
            if (title.isBlank()) throw new RuntimeException("title cannot be blank");

            Long examId = t.getExam() != null ? t.getExam().getId() : null;
            if (examId != null &&
                testRepo.existsByExamIdAndTitleIgnoreCaseAndIdNot(examId, title, id)) {
                throw new RuntimeException("A test with this title already exists for the exam");
            }
            t.setTitle(title);
        }

        return toResponse(t);
    }

    @Override
    public void deleteTest(Long id) {
        if (!testRepo.existsById(id)) throw new RuntimeException("Test not found");
        testRepo.deleteById(id); // grades will be removed if you set cascade+orphanRemoval on Test.grades
    }

    @Override
    public List<TestResponse> searchByTitle(String titleLike) {
        String q = safe(titleLike);
        if (q == null || q.isBlank()) return List.of();
        return testRepo.findByTitleContainingIgnoreCase(q)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<TestResponse> listByExam(Long examId) {
        if (examId == null) throw new RuntimeException("examId is required");
        return testRepo.findByExamId(examId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // Optional combined search: by exam AND partial title
    @Override
    public List<TestResponse> searchByExamAndTitle(Long examId, String titleLike) {
        if (examId == null) throw new RuntimeException("examId is required");
        String q = safe(titleLike);
        if (q == null || q.isBlank()) {
            return listByExam(examId);
        }
        return testRepo.findByExamIdAndTitleContainingIgnoreCase(examId, q)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    // ---- helpers ----
    private TestResponse toResponse(Test t) {
        var exam = t.getExam();
        return new TestResponse(
                t.getId(),
                exam != null ? exam.getId() : null,
                t.getTitle()
                
               
            
        );
    }

    private String safe(String s) { return s == null ? null : s.trim(); }
}
