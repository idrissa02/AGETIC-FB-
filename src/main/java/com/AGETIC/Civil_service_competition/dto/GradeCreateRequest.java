package com.AGETIC.Civil_service_competition.dto;

import com.AGETIC.Civil_service_competition.model.Admin;

public record GradeCreateRequest(

        Long candidateId,
        Long testId,
        Double score,
        String correctionReason,
        Long gradedBy // or Long gradedById if you want to only pass ID
) {}
