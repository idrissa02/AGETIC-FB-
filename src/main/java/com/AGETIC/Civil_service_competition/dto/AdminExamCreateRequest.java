// AdminExamCreateRequest.java
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;
import java.util.List;

public record AdminExamCreateRequest(
    Long adminId,                 // optional: set Exam.createdBy if you have it
    String title,
    LocalDate examDate,
    LocalDate applicationDeadline,
    String conditions,            // nullable
    Integer quota,                // nullable
    Integer hours,                // nullable
    Long categoryId,              // nullable, only if Exam has category
    List<String> tests            // optional: create tests at once
) {}
