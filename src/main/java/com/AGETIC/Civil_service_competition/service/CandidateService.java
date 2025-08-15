package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.CandidateResponse;

public interface CandidateService {

    // CandidateResponse createFromApplication(Long applicationId);            // generate number, link app
    CandidateResponse getByCandidateNumber(String candidateNumber);
    CandidateResponse setStatus(String candidateNumber, String status);     // ADMITTED / NOT_ADMITTED / PENDING
}
