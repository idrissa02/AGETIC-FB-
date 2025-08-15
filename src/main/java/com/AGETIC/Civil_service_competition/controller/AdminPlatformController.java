package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.AddTestsRequest;
import com.AGETIC.Civil_service_competition.dto.AdminExamCreateRequest;
import com.AGETIC.Civil_service_competition.dto.AdminExamUpdateRequest;
import com.AGETIC.Civil_service_competition.dto.AdminGradeRequest;
import com.AGETIC.Civil_service_competition.dto.CandidateProfileResponse;
import com.AGETIC.Civil_service_competition.dto.ExamResponse;
import com.AGETIC.Civil_service_competition.dto.GradeResponse;
import com.AGETIC.Civil_service_competition.dto.TestMiniResponse;
import com.AGETIC.Civil_service_competition.service.AdminPlatformService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminPlatformController {

    private final AdminPlatformService service;

    public AdminPlatformController(AdminPlatformService service) {
        this.service = service;
    }

    // -------------------- Exams --------------------

    @PostMapping("/exams")
    public ExamResponse createExam(@RequestBody @Valid AdminExamCreateRequest request) {
        return service.createExam(request);
    }

    @PutMapping("/exams/{examId}")
    public ExamResponse updateExam(@PathVariable Long examId,
                                   @RequestBody @Valid AdminExamUpdateRequest request) {
        return service.updateExam(examId, request);
    }

    @DeleteMapping("/exams/{examId}")
    public void deleteExam(@PathVariable Long examId) {
        service.deleteExam(examId);
    }

    @GetMapping("/exams")
    public List<ExamResponse> listExams() {
        return service.listExams();
    }

    @GetMapping("/exams/{examId}/tests")
    public List<TestMiniResponse> listTestsOfExam(@PathVariable Long examId) {
        return service.listTestsOfExam(examId);
    }

    @PostMapping("/exams/{examId}/tests")
    public List<TestMiniResponse> addTests(@PathVariable Long examId,
                                           @RequestBody @Valid AddTestsRequest request) {
        return service.addTests(examId, request);
    }

    // -------------------- Grading --------------------

    @PostMapping("/grades")
    public GradeResponse gradeCandidate(@RequestBody @Valid AdminGradeRequest request) {
        return service.gradeCandidate(request);
    }

    @GetMapping("/grades/by-candidate/{candidateId}")
    public List<GradeResponse> listGradesByCandidate(@PathVariable Long candidateId) {
        return service.listGradesByCandidate(candidateId);
    }

    @GetMapping("/grades/by-exam/{examId}")
    public List<GradeResponse> listGradesByExam(@PathVariable Long examId) {
        return service.listGradesByExam(examId);
    }

    // -------------------- Candidate Profile --------------------

    @GetMapping("/candidates/{candidateId}")
    public CandidateProfileResponse getCandidateProfile(@PathVariable Long candidateId) {
        return service.getCandidateProfile(candidateId);
    }
}

