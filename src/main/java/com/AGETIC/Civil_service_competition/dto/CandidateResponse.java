
package com.AGETIC.Civil_service_competition.dto;

public record CandidateResponse(
        
        Long id,
        String candidateNumber,
        String status,
        Long applicationId
) {}
