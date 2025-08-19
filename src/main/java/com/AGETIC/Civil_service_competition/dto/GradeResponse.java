package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDateTime;

import com.AGETIC.Civil_service_competition.model.Admin;

public record GradeResponse(
    
        Long candidateId,
        Long testId,
        Double score,
        LocalDateTime gradedAt,
        LocalDateTime updatedAt,
        String correctionReason,
        Long gradedById
) {}
