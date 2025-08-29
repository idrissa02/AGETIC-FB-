// dto/ExamForm.java
package com.AGETIC.Civil_service_competition.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record ExamForm(
    Long id,

    @NotBlank String title,

    @NotNull LocalDate date,

    @NotNull LocalDate applicationDeadline,

    // NEW
    @NotBlank String condition,

    @NotNull @Min(1) Integer quota,

    @NotNull @Min(1) Integer hours,

    @NotNull Long categoryId,

    // NEW (can be filled from security or left null and set in service)
    Long createdByAdminId
) {}


