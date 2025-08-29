package com.AGETIC.Civil_service_competition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TestForm(
    Long id,

    @NotNull(message = "L’examen est obligatoire")
    Long examId,

    @NotBlank(message = "Le titre est obligatoire")
    String title
) {}
