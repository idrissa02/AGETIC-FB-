package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.CandidateResponse;
import com.AGETIC.Civil_service_competition.Enum.CandidateStatus;
import com.AGETIC.Civil_service_competition.Enum.ApplicationStatus;
import com.AGETIC.Civil_service_competition.model.Application;
import com.AGETIC.Civil_service_competition.model.Candidate;
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
    public CandidateResponse setStatus(String candidateNumber, String status) {
        var opt = candidateRepo.findByCandidateNumber(candidateNumber);
        if (opt.isEmpty()) return null;

        Candidate c = opt.get();
        try {
            c.setStatus(CandidateStatus.valueOf(status.toUpperCase())); // ADMITTED / NOT_ADMITTED / PENDING
        } catch (IllegalArgumentException e) {
            return null;
        }
        return toResponse(c);
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
