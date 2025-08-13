// dto/ExamUpdateRequest.java
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;

public record ExamUpdateRequest(
        
        String title,
        LocalDate date,
        LocalDate applicationDeadline,
        String condition,
        Integer quota,
        Integer hours,
        Long categoryId
) {}
