// AdminGradeRequest.java
package com.AGETIC.Civil_service_competition.dto;

public record AdminGradeRequest(
    Long adminId,            // optional; stored in Grade.gradedBy (e.g., username) if you want
    Long candidateId,
    Long testId,
    Double score,
    String correctionReason  // optional
) {}
