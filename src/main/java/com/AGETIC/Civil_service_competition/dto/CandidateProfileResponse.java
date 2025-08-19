// CandidateProfileResponse.java
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;
import java.util.List;

public record CandidateProfileResponse(

    
     Long applicationId,
        String status,                 // PENDING / REFUSED / ACCEPTED / ADMITTED / NOT_ADMITTED
        String candidateNumber,        // null unless accepted
        String ninaMasked,
        String name,
        String surname,
        String emailMasked,
        String phoneMasked,
        LocalDate birthDate,

        Long examId,                   // null unless accepted
        String examTitle,
        LocalDate examDate,

        List<TestMiniResponse> tests,          // empty in applicationId view (unless you want otherwise)
        List<GradeResponse> grades,    // empty in applicationId view
        Double finalScore    
) {}
