package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.CandidateResponse;
import com.AGETIC.Civil_service_competition.Enum.CandidateStatus;
import com.AGETIC.Civil_service_competition.Enum.ApplicationStatus;
import com.AGETIC.Civil_service_competition.model.Application;
import com.AGETIC.Civil_service_competition.model.Candidate;
import com.AGETIC.Civil_service_competition.model.Grade;
import com.AGETIC.Civil_service_competition.repository.ApplicationRepository;
import com.AGETIC.Civil_service_competition.repository.CandidateRepository;
import com.AGETIC.Civil_service_competition.service.CandidateService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepo;

    public CandidateServiceImpl(CandidateRepository candidateRepo) {
        this.candidateRepo = candidateRepo;

    }

    @Override
    public CandidateResponse getByCandidateNumber(String candidateNumber) {
        return candidateRepo.findByCandidateNumber(candidateNumber)
                .map(this::toResponse).orElse(null);
    }

    @Override
    public double computeFinalScore(Long candidateId) {
        var grades = gradeRepo.findByCandidateId(candidateId); // inject GradeRepository here
        if (grades.isEmpty()) {
            return 0.0;
        }
        double avg = grades.stream().mapToDouble(Grade::getScore).average().orElse(0.0);
        // cache if you added finalScore
        candidateRepo.findById(candidateId).ifPresent(c -> c.setFinalScore(avg));
        return avg;
    }

    @Override
    public void autoUpdateStatus(Long candidateId) {
        double score = computeFinalScore(candidateId);
        candRepo.findById(candidateId).ifPresent(c -> {
            c.setStatus(score >= 60.0 ? CandidateStatus.ADMITTED : CandidateStatus.REFUSED);
        });
    }

    // ---- helpers
    private CandidateResponse toResponse(Candidate c) {
        return new CandidateResponse(
                c.getId(),
                c.getCandidateNumber(),
                c.getStatus().name(),
                c.getApplication() != null ? c.getApplication().getId() : null
        );
    }

}
