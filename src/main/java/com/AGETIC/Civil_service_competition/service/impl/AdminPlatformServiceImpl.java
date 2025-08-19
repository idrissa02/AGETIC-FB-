package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.model.*;
import com.AGETIC.Civil_service_competition.repository.*;
import com.AGETIC.Civil_service_competition.service.AdminPlatformService;

import jakarta.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class AdminPlatformServiceImpl implements AdminPlatformService {

    private final ExamRepository examRepo;
    private final TestRepository testRepo;
    private final CandidateRepository candidateRepo;
    private final GradeRepository gradeRepo;
    private final AdminRepository adminRepo;
        private final CenterRepository centerRepo ;

    public AdminPlatformServiceImpl(
            ExamRepository examRepo,
            TestRepository testRepo,
            CandidateRepository candidateRepo,
            GradeRepository gradeRepo,
            AdminRepository adminRepo,
            CenterRepository centerRepo
    ) {
        this.examRepo = examRepo;
        this.testRepo = testRepo;
        this.candidateRepo = candidateRepo;
        this.gradeRepo = gradeRepo;
        this.adminRepo = adminRepo;
        this.centerRepo = centerRepo;
    }

    // ----------------- EXAMS -----------------

    @Override
    public ExamResponse createExam(AdminExamCreateRequest req) {
        if(examRepo.existsByTitleAndDate(req.title(), req.examDate())) {
            throw new RuntimeException("Exam with same title and date already exists");
        }
        Exam e = new Exam();
        e.setTitle(req.title());
        e.setDate(req.examDate());
        e.setApplicationDeadline(req.applicationDeadline());
        e.setCondition(req.conditions());
        e.setQuota(req.quota());
        e.setHours(req.hours());

        if (req.categoryId() != null) {
            Category cat = new Category();
            cat.setId(req.categoryId());
            e.setCategory(cat);
        }

        if (req.adminId() != null) {
            adminRepo.findById(req.adminId()).ifPresent(e::setCreatedBy);
        }

        examRepo.save(e);

        // optional: add tests
        if (req.tests() != null && !req.tests().isEmpty()) {
            for (String t : req.tests()) {
                Test test = new Test();
                test.setExam(e);
                test.setTitle(t);
                testRepo.save(test);
            }
        }

        return toExamResponse(e);
    }

    @Override
    public ExamResponse updateExam(Long examId, AdminExamUpdateRequest req) {
        Exam e = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        e.setTitle(req.title());
        e.setDate(req.examDate());
        e.setApplicationDeadline(req.applicationDeadline());
        e.setCondition(req.conditions());
        e.setQuota(req.quota());
        e.setHours(req.hours());
        if (req.categoryId() != null) {
            Category cat = new Category();
            cat.setId(req.categoryId());
            e.setCategory(cat);
        }
        return toExamResponse(e);
    }

    @Override
    public void deleteExam(Long examId) {
        examRepo.deleteById(examId);
    }

    @Override
    public List<ExamResponse> listExams() {
        return examRepo.findAll().stream()
                .map(this::toExamResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<TestMiniResponse> listTestsOfExam(Long examId) {
        return testRepo.findByExamId(examId).stream()
                .map(t -> new TestMiniResponse(t.getId(), t.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TestMiniResponse> addTests(Long examId, AddTestsRequest req) {
        Exam e = examRepo.findById(examId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return req.titles().stream().map(title -> {
            Test t = new Test();
            t.setTitle(title);
            t.setExam(e);
            testRepo.save(t);
            return new TestMiniResponse(t.getId(), t.getTitle());
        }).collect(Collectors.toList());
    }

    // ----------------- GRADING -----------------

    @Override
    public GradeResponse gradeCandidate(AdminGradeRequest req) {
        Candidate c = candidateRepo.findById(req.candidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        Test t = testRepo.findById(req.testId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        Grade grade = gradeRepo.findByCandidateIdAndTestId(c.getId(), t.getId())
                .orElse(new Grade(c, t, req.score(), null));

        grade.setScore(req.score());
        grade.setCorrectionReason(req.correctionReason());
        grade.setUpdatedAt(LocalDateTime.now());

        if (req.adminId() != null) {
            adminRepo.findById(req.adminId()).ifPresent(grade::setGradedBy);
        }

        gradeRepo.save(grade);

        return toGradeResponse(grade);
    }

    @Override
    public List<GradeResponse> listGradesByCandidate(Long candidateId) {
        return gradeRepo.findByCandidateId(candidateId).stream()
                .map(this::toGradeResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeResponse> listGradesByExam(Long examId) {
        return gradeRepo.findByCandidate_Application_Exam_Id(examId).stream()
                .map(this::toGradeResponse)
                .collect(Collectors.toList());
    }

    // ----------------- CANDIDATE PROFILE -----------------

    @Override
    public CandidateProfileResponse getCandidateProfile(String candidateNumber) {
       Candidate c = candidateRepo.findByCandidateNumber(candidateNumber)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Candidate not found"));

    Application app = Optional.ofNullable(c.getApplication())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No application linked to candidate"));

    Exam exam = Optional.ofNullable(app.getExam())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No exam linked to application"));

        List<TestMiniResponse> tests = testRepo.findByExamId(exam.getId()).stream()
                .map(t -> new TestMiniResponse(t.getId(), t.getTitle()))
                .collect(Collectors.toList());

        List<GradeResponse> grades = gradeRepo.findByCandidateId(c.getId()).stream()
                .map(this::toGradeResponse)
                .collect(Collectors.toList());

        Double avg = grades.isEmpty() ? null :
                grades.stream().mapToDouble(GradeResponse::score).average().orElse(0);

        return new CandidateProfileResponse(
                app.getId(),
                c.getStatus().name(),
                c.getCandidateNumber(),
                mask(app.getNinaNumber()),
                app.getName(),
                app.getSurname(),
                mask(app.getEmail()),
                mask(app.getPhone()),
                app.getBirthDate(),
                exam.getId(),
                exam.getTitle(),
                exam.getDate(),
                tests,
                grades,
                avg
        );
    }


    // exam publishing

@Override
public void publishExam(Long examId, boolean published) {
    Exam e = examRepo.findById(examId)
            .orElseThrow(() -> new RuntimeException("Exam not found"));
    e.setResultsPublished(published);
    // examRepo.save(e); // not needed if entity is managed in TX
}



// Exporting exam results to XLSX
@Override
public byte[] exportExamResultsXlsx(Long examId) {
    Exam exam = examRepo.findById(examId)
            .orElseThrow(() -> new RuntimeException("Exam not found"));

    List<Test> tests = testRepo.findByExamId(examId);
    List<Candidate> candidates = candidateRepo.findAllByApplication_Exam_Id(examId);

    try (org.apache.poi.xssf.usermodel.XSSFWorkbook wb = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
        org.apache.poi.ss.usermodel.Sheet sheet = wb.createSheet("Results");

        int r = 0;
        org.apache.poi.ss.usermodel.Row head = sheet.createRow(r++);
        int c = 0;
        head.createCell(c++).setCellValue("Candidate Number");
        head.createCell(c++).setCellValue("Name");
        head.createCell(c++).setCellValue("Surname");
        for (Test t : tests) head.createCell(c++).setCellValue(t.getTitle());
        head.createCell(c++).setCellValue("Average");
        head.createCell(c++).setCellValue("Status");

        for (Candidate cand : candidates) {
            Application app = cand.getApplication();
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(r++);
            int i = 0;

            row.createCell(i++).setCellValue(cand.getCandidateNumber());
            row.createCell(i++).setCellValue(app.getName());
            row.createCell(i++).setCellValue(app.getSurname());

            List<Grade> grades = gradeRepo.findByCandidateId(cand.getId());
            java.util.Map<Long, Double> byTest = new java.util.HashMap<>();
            for (Grade g : grades) byTest.put(g.getTest().getId(), g.getScore());

            double sum = 0.0;
            int cnt = 0;

            for (Test t : tests) {
                Double sc = byTest.get(t.getId());
                org.apache.poi.ss.usermodel.Cell cell = row.createCell(i++);
                if (sc != null) {
                    cell.setCellValue(sc); // numeric
                    sum += sc;
                    cnt++;
                } else {
                    cell.setCellValue(""); // blank
                }
            }

            Double avg = (cnt == 0) ? null : (sum / cnt);
            org.apache.poi.ss.usermodel.Cell avgCell = row.createCell(i++);
            if (avg != null) {
                avgCell.setCellValue(avg);
            } else {
                avgCell.setCellValue("");
            }

            String status = (avg != null && avg >= 60.0) ? "ADMITTED" : "NOT_ADMITTED";
            row.createCell(i).setCellValue(status);
        }

        int totalCols = 3 + tests.size() + 2; // number, name, surname + tests + average/status
        for (int col = 0; col < totalCols; col++) sheet.autoSizeColumn(col);

        try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
            wb.write(out);
            return out.toByteArray();
        }
    } catch (Exception e) {
        throw new RuntimeException("Export failed", e);
    }
}

//Assign centers to an exam
@Override
public void assignCenters(ExamCenterAssignRequest req) {
    Exam exam = examRepo.findById(req.examId())
            .orElseThrow(() -> new RuntimeException("Exam not found"));

    var set = new java.util.HashSet<Center>();
    for (Long cid : req.centerIds()) {
        Center c = centerRepo.findById(cid)
                .orElseThrow(() -> new RuntimeException("Center not found: " + cid));
        set.add(c);
    }
    exam.getCenters().clear();
    exam.getCenters().addAll(set); // managed entity; persists on tx commit
}

@Override
public java.util.List<CenterResponse> listCentersOfExam(Long examId) {
    Exam exam = examRepo.findById(examId)
            .orElseThrow(() -> new RuntimeException("Exam not found"));
    return exam.getCenters().stream()
            .map(c -> new CenterResponse(c.getId(), c.getName(), c.getLocation()))
            .toList();
}




    // ----------------- HELPERS -----------------

    private ExamResponse toExamResponse(Exam e) {
        return new ExamResponse(
                e.getId(),
                e.getTitle(),
                e.getDate(),
                e.getApplicationDeadline(),
                e.getQuota(),
                e.getHours(),
                e.getCategory() != null ? e.getCategory().getId() : null,
                e.getCreatedBy() != null ? e.getCreatedBy().getId() : null,
                (e.getTests() != null) ? e.getTests().size() : 0,
                e.isResultsPublished()
        );
    }

    private GradeResponse toGradeResponse(Grade g) {
        return new GradeResponse(
                g.getCandidate().getId(),
                g.getTest().getId(),
                g.getScore(),
                g.getGradedAt(),
                g.getUpdatedAt(),
                g.getCorrectionReason(),
                g.getGradedBy() != null ? g.getGradedBy().getId() : null
        );
    }

    private String mask(String value) {
        if (value == null) return null;
        return value.length() <= 4 ? "****" : value.substring(0, 2) + "****";
    }
}
