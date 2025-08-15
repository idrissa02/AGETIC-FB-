// AdminPlatformServiceImpl.java
package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.model.*;
import com.AGETIC.Civil_service_competition.repository.*;
import com.AGETIC.Civil_service_competition.service.AdminPlatformService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AdminPlatformServiceImpl implements AdminPlatformService {

    private final ExamRepository examRepo;
    private final TestRepository testRepo;
    private final CandidateRepository candidateRepo;
    private final ApplicationRepository applicationRepo;
    private final GradeRepository gradeRepo;
    private final AdminRepository adminRepo;           // optional; only if you want to resolve gradedBy username
    // private final CategoryRepository categoryRepo;  // if you have Category mapping

    public AdminPlatformServiceImpl(
            ExamRepository examRepo,
            TestRepository testRepo,
            CandidateRepository candidateRepo,
            ApplicationRepository applicationRepo,
            GradeRepository gradeRepo,
            AdminRepository adminRepo
            // , CategoryRepository categoryRepo
    ) {
        this.examRepo = examRepo;
        this.testRepo = testRepo;
        this.candidateRepo = candidateRepo;
        this.applicationRepo = applicationRepo;
        this.gradeRepo = gradeRepo;
        this.adminRepo = adminRepo;
        // this.categoryRepo = categoryRepo;
    }

    // ------------------- Exams -------------------

    @Override
    public ExamResponse createExam(AdminExamCreateRequest req) {
        if (isBlank(req.title())) throw new RuntimeException("title is required");
        if (req.examDate() == null) throw new RuntimeException("examDate is required");
        if (req.applicationDeadline() == null) throw new RuntimeException("applicationDeadline is required");

        Exam e = new Exam();
        e.setTitle(req.title().trim());
        e.setDate(req.examDate());
        e.setApplicationDeadline(req.applicationDeadline());
        e.setQuota(req.quota());
        e.setHours(req.hours());
        e.setUpdatedAt(LocalDateTime.now());
        if (req.conditions() != null) e.setCondition(req.conditions().trim());

        // If you have category in Exam
        // if (req.categoryId() != null) {
        //     Category cat = categoryRepo.findById(req.categoryId())
        //             .orElseThrow(() -> new RuntimeException("Category not found"));
        //     e.setCategory(cat);
        // }

        // If your Exam has createdBy field and you want to set it:
        // if (req.adminId() != null) {
        //     Admin admin = adminRepo.findById(req.adminId())
        //             .orElseThrow(() -> new RuntimeException("Admin not found"));
        //     e.setCreatedBy(admin);
        // }

        examRepo.save(e);

        // optional bulk tests
        if (req.tests() != null && !req.tests().isEmpty()) {
            for (String t : req.tests()) {
                String title = safe(t);
                if (isBlank(title)) continue;
                if (testRepo.existsByExamIdAndTitleIgnoreCase(e.getId(), title)) continue;
                Test test = new Test();
                test.setExam(e);
                test.setTitle(title);
                testRepo.save(test);
            }
        }
        return toExamResponse(e);
    }

    @Override
    public ExamResponse updateExam(Long examId, AdminExamUpdateRequest req) {
        Exam e = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        if (req.title() != null) {
            String t = safe(req.title());
            if (isBlank(t)) throw new RuntimeException("title cannot be blank");
            e.setTitle(t);
        }
        if (req.examDate() != null) e.setDate(req.examDate());
        if (req.applicationDeadline() != null) e.setApplicationDeadline(req.applicationDeadline());
        if (req.conditions() != null) e.setCondition(safe(req.conditions()));
        if (req.quota() != null) e.setQuota(req.quota());
        if (req.hours() != null) e.setHours(req.hours());

        // if (req.categoryId() != null) {
        //     Category cat = categoryRepo.findById(req.categoryId())
        //             .orElseThrow(() -> new RuntimeException("Category not found"));
        //     e.setCategory(cat);
        // }

        e.setUpdatedAt(LocalDateTime.now());
        return toExamResponse(e);
    }

    @Override
    public void deleteExam(Long examId) {
        if (!examRepo.existsById(examId)) throw new RuntimeException("Exam not found");
        // Consider FK cascade for tests/grades or block deletion if tests exist
        examRepo.deleteById(examId);
    }

    @Override
    public List<ExamResponse> listExams() {
        return examRepo.findAll().stream().map(this::toExamResponse).toList();
    }

    @Override
    public List<TestMiniResponse> listTestsOfExam(Long examId) {
        if (!examRepo.existsById(examId)) throw new RuntimeException("Exam not found");
        return testRepo.findByExamId(examId).stream().map(this::toTestMini).toList();
    }

    @Override
    public List<TestMiniResponse> addTests(Long examId, AddTestsRequest req) {
        Exam e = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        if (req == null || req.titles() == null || req.titles().isEmpty())
            throw new RuntimeException("titles are required");

        List<TestMiniResponse> created = new ArrayList<>();
        for (String t : req.titles()) {
            String title = safe(t);
            if (isBlank(title)) continue;
            if (testRepo.existsByExamIdAndTitleIgnoreCase(e.getId(), title)) continue;
            Test test = new Test();
            test.setExam(e);
            test.setTitle(title);
            testRepo.save(test);
            created.add(toTestMini(test));
        }
        return created;
    }

    // ------------------- Grading -------------------

    @Override
    public GradeResponse gradeCandidate(AdminGradeRequest req) {
        if (req.candidateId() == null) throw new RuntimeException("candidateId is required");
        if (req.testId() == null) throw new RuntimeException("testId is required");
        if (req.score() == null) throw new RuntimeException("score is required");

        Candidate cand = candidateRepo.findById(req.candidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        Test test = testRepo.findById(req.testId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        GradeId id = new GradeId(cand.getId(), test.getId());
        Grade g = gradeRepo.findById(id).orElse(new Grade());

        g.setId(id);
        g.setCandidate(cand);
        g.setTest(test);
        g.setScore(req.score());
        g.setGradedAt(LocalDateTime.now());
        if (req.adminId() != null) {
            Admin a = adminRepo.findById(req.adminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
            // store admin username (or id) in gradedBy string
            g.setGradedBy(a);
        }
        if (req.correctionReason() != null) {
            g.setCorrectionReason(req.correctionReason());
        }

        gradeRepo.save(g);
        return toGradeResponse(g);
    }

    @Override
    public List<GradeResponse> listGradesByCandidate(Long candidateId) {
        candidateRepo.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        return gradeRepo.findByCandidateId(candidateId).stream().map(this::toGradeResponse).toList();
    }

    @Override
    public List<GradeResponse> listGradesByExam(Long examId) {
        examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return gradeRepo.findByTestExamId(examId).stream().map(this::toGradeResponse).toList();
    }

    // ------------------- Candidate view -------------------

    @Override
    public CandidateProfileResponse getCandidateProfile(Long candidateId) {
        Candidate c = candidateRepo.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Application app = c.getApplication(); // assuming you mapped OneToOne
        List<GradeResponse> grades = gradeRepo.findByCandidateId(candidateId)
                .stream().map(this::toGradeResponse).toList();

        return new CandidateProfileResponse(
                c.getId(),
                /* candidate number */ getSafeCandidateNumber(c),
                /* status */ c.getStatus() != null ? c.getStatus().name() : null,
                app != null ? app.getId() : null,
                app != null ? app.getNinaNumber() : null,
                app != null ? app.getName() : null,
                app != null ? app.getSurname() : null,
                app != null ? app.getEmail() : null,
                app != null ? app.getPhone() : null,
                app != null ? app.getBirthDate() : null,
                grades
        );
    }

    // ------------------- helpers -------------------

    private ExamResponse toExamResponse(Exam e) {
    Long categoryId = (e.getCategory() != null) ? e.getCategory().getId() : null;
    Long createdByAdminId = (e.getCreatedBy() != null) ? e.getCreatedBy().getId() : null;

    int testsCount = (int) testRepo.countByExamId(e.getId()); // better than loading list
    return new ExamResponse(
        e.getId(),
        e.getTitle(),
        e.getDate(),
        e.getApplicationDeadline(),
        e.getQuota(),
        e.getHours(),
        categoryId,
        createdByAdminId,
        testsCount
    );
}


    private TestMiniResponse toTestMini(Test t) {
        return new TestMiniResponse(t.getId(), t.getTitle());
    }

   private GradeResponse toGradeResponse(Grade g) {
    Long candId = (g.getId() != null) ? g.getId().getCandidateId()
                 : (g.getCandidate() != null ? g.getCandidate().getId() : null);

    Long testId = (g.getId() != null) ? g.getId().getTestId()
                 : (g.getTest() != null ? g.getTest().getId() : null);

    return new GradeResponse(
        candId,
        testId,
        g.getScore(),
        g.getGradedAt(),
        g.getGradedBy(),
        g.getCorrectionReason()
    );
}


    private String getSafeCandidateNumber(Candidate c) {
        try { return c.getCandidateNumber(); }
        catch (Exception ignore) { return null; }
    }

    private String safe(String s) { return s == null ? null : s.trim(); }
    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
