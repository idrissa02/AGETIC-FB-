// src/main/java/com/AGETIC/Civil_service_competition/dto/ClassroomForm.java
package com.AGETIC.Civil_service_competition.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ClassroomForm(
    Long id,
    @NotBlank(message = "Le numéro est obligatoire") String number,
    @Min(value = 1, message = "La capacité doit être > 0") int capacity,
    @NotNull(message = "Le centre est obligatoire") Long centerId
) {}

