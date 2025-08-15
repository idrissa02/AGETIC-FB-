package com.AGETIC.Civil_service_competition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TestRequest(
    @NotNull(message = "examId is required")
    Long examId,

    @NotBlank(message = "title is required")
    @Size(max = 200, message = "title must be at most 200 characters")
    String title
) {}

