
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;

public record ExamCreateRequest(
        
        String title,
        LocalDate date,
        LocalDate applicationDeadline,
        String condition,
        Integer quota,
        Integer hours,
        Long categoryId,
        Long createdByAdminId
) {}
