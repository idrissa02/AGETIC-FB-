// dto/ExamResponse.java
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;

public record ExamResponse(
        Long id,
        String title,
        LocalDate date,
        LocalDate applicationDeadline,
        Integer quota,
        Integer hours,
        Long categoryId,
        Long createdByAdminId,
        Integer testsCount,
        boolean resultsPublished
) {}
