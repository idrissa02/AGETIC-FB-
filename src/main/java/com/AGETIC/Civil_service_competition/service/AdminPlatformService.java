// AdminPlatformService.java
package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.*;

import java.util.List;

public interface AdminPlatformService {

    // Exams
    ExamResponse createExam(AdminExamCreateRequest request);
    ExamResponse updateExam(Long examId, AdminExamUpdateRequest request);
    void deleteExam(Long examId);
    List<ExamResponse> listExams();
    List<TestMiniResponse> listTestsOfExam(Long examId);
    List<TestMiniResponse> addTests(Long examId, AddTestsRequest request);

    // Grading
    GradeResponse gradeCandidate(AdminGradeRequest request);         // create or overwrite
    List<GradeResponse> listGradesByCandidate(Long candidateId);
    List<GradeResponse> listGradesByExam(Long examId);

    // Candidate view
    CandidateProfileResponse getCandidateProfile(Long candidateId);
}
