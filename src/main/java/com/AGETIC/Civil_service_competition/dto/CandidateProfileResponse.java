// CandidateProfileResponse.java
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;
import java.util.List;

public record CandidateProfileResponse(
    Long candidateId,
    String candidateNumber,
    String status,
    // application basics
    Long applicationId,
    String ninaNumber,
    String name,
    String surname,
    String email,
    String phone,
    LocalDate birthDate,
    // summary
    List<GradeResponse> grades
) {}
