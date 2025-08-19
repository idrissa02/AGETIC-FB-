// src/main/java/com/AGETIC/Civil_service_competition/controller/AdminPlatformController.java
package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.*;
import com.AGETIC.Civil_service_competition.service.AdminPlatformService;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminPlatformController {

    private final AdminPlatformService service;

    public AdminPlatformController(AdminPlatformService service) {
        this.service = service;
    }

    // ================= EXAMS =================
    @PostMapping("/exams")
    public ResponseEntity<ExamResponse> createExam(@RequestBody AdminExamCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.createExam(req));
    }

    @PutMapping("/exams/{examId}")
    public ResponseEntity<ExamResponse> updateExam(@PathVariable Long examId,
            @RequestBody AdminExamUpdateRequest req) {
        return ResponseEntity.ok(service.updateExam(examId, req));
    }

    @DeleteMapping("/exams/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId) {
        service.deleteExam(examId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exams")
    public ResponseEntity<List<ExamResponse>> listExams() {
        return ResponseEntity.ok(service.listExams());
    }

    // tests of an exam
    @GetMapping("/exams/{examId}/tests")
    public ResponseEntity<List<TestMiniResponse>> listTests(@PathVariable Long examId) {
        return ResponseEntity.ok(service.listTestsOfExam(examId));
    }

    @PostMapping("/exams/{examId}/tests")
    public ResponseEntity<List<TestMiniResponse>> addTests(@PathVariable Long examId,
            @RequestBody AddTestsRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.addTests(examId, req));
    }

    // ================= GRADING =================
    @PostMapping("/grades")
    public ResponseEntity<GradeResponse> gradeCandidate(@RequestBody AdminGradeRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.gradeCandidate(req));
    }

    @GetMapping("/grades/candidate/{candidateId}")
    public ResponseEntity<List<GradeResponse>> listGradesByCandidate(@PathVariable Long candidateId) {
        return ResponseEntity.ok(service.listGradesByCandidate(candidateId));
    }

    @GetMapping("/grades/exams/{examId}")
    public ResponseEntity<List<GradeResponse>> listGradesByExam(@PathVariable Long examId) {
        return ResponseEntity.ok(service.listGradesByExam(examId));
    }




    // ================= CANDIDATE PROFILE (admin view) =================


    @GetMapping("/candidates/{candidateNumber}/profile")
    public ResponseEntity<CandidateProfileResponse> candidateProfile(@PathVariable String candidateNumber) {
        return ResponseEntity.ok(service.getCandidateProfile(candidateNumber));
    }

    @PatchMapping("/exams/{examId}/publish")
    public ResponseEntity<Void> publish(@PathVariable Long examId, @RequestParam boolean published) {
        service.publishExam(examId, published);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exams/{examId}/results.xlsx")
    public ResponseEntity<byte[]> export(@PathVariable Long examId) {
        byte[] bytes = service.exportExamResultsXlsx(examId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=exam-" + examId + "-results.xlsx")
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(bytes);
    }

//================= EXAM CENTERS =================

    @PostMapping("/exams/{examId}/centers")
    public ResponseEntity<Void> assignCenters(@PathVariable Long examId,
            @RequestBody ExamCenterAssignRequest req) {
        if (req.examId() == null || !req.examId().equals(examId)) {
            return ResponseEntity.badRequest().build();
        }
        service.assignCenters(req);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exams/{examId}/centers")
    public ResponseEntity<List<CenterResponse>> listCenters(@PathVariable Long examId) {
        return ResponseEntity.ok(service.listCentersOfExam(examId));
    }

}
