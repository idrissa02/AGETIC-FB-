package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.CandidateProfileResponse;
import com.AGETIC.Civil_service_competition.dto.CandidateResponse;

public interface CandidateService {

    double computeFinalScore(Long candidateId);                 // average of scores

    void autoUpdateStatus(Long candidateId);                    // >=60 -> ADMITTED else NOT_ADMITTED

    CandidateResponse getByCandidateNumber(String candidateNumber); // controller uses this; you can hide score until published

    CandidateProfileResponse viewByApplicationId(Long applicationId);        // infos; +exam+candidateNumber only if ACCEPTED

    CandidateProfileResponse viewByCandidateNumber(String candidateNumber);  // ALL infos + tests + grades (only if published)
}
