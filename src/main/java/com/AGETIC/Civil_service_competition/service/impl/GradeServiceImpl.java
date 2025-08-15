package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.GradeCreateRequest;
import com.AGETIC.Civil_service_competition.dto.GradeResponse;
import com.AGETIC.Civil_service_competition.model.*;
import com.AGETIC.Civil_service_competition.repository.GradeRepository;
import com.AGETIC.Civil_service_competition.repository.CandidateRepository;
import com.AGETIC.Civil_service_competition.repository.TestRepository;
import com.AGETIC.Civil_service_competition.service.GradeService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepo;
    private final CandidateRepository candidateRepo;
    private final TestRepository testRepo;

    public GradeServiceImpl(GradeRepository gradeRepo,
                            CandidateRepository candidateRepo,
                            TestRepository testRepo) {
        this.gradeRepo = gradeRepo;
        this.candidateRepo = candidateRepo;
        this.testRepo = testRepo;
    }

    @Override
    public GradeResponse assignGrade(GradeCreateRequest request) {
        Candidate candidate = candidateRepo.findById(request.candidateId())
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
        Test test = testRepo.findById(request.testId())
                .orElseThrow(() -> new RuntimeException("Test not found"));

        if (gradeRepo.existsByCandidateIdAndTestId(candidate.getId(), test.getId())) {
            throw new RuntimeException("Grade already assigned");
        }

        Grade grade = new Grade(candidate, test, request.score(), request.gradedBy());
        grade.setGradedAt(LocalDateTime.now());
        grade.setUpdatedAt(LocalDateTime.now());
        gradeRepo.save(grade);

        return toResponse(grade);
    }

    @Override
    public List<GradeResponse> getGradesForCandidate(Long candidateId) {
        return gradeRepo.findByCandidateId(candidateId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeResponse> getGradesForTest(Long testId) {
        return gradeRepo.findByTestId(testId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GradeResponse updateGrade(GradeCreateRequest request) {
        Grade grade = gradeRepo.findByCandidateIdAndTestId(request.candidateId(), request.testId())
                .orElseThrow(() -> new RuntimeException("Grade not found"));

        grade.setScore(request.score());
        grade.setCorrectionReason(request.correctionReason());
        grade.setUpdatedAt(LocalDateTime.now());
        gradeRepo.save(grade);

        return toResponse(grade);
    }

    @Override
    public void deleteGrade(Long candidateId, Long testId) {
        GradeId id = new GradeId(candidateId, testId);
        gradeRepo.deleteById(id);
    }

    // Helper
    private GradeResponse toResponse(Grade grade) {
        return new GradeResponse(
                grade.getCandidate().getId(),
                grade.getTest().getId(),
                grade.getScore(),
                grade.getGradedAt(),
                grade.getUpdatedAt(),
                grade.getCorrectionReason(),
                grade.getGradedBy() != null ? grade.getGradedBy().getId() : null
        );
    }
}
