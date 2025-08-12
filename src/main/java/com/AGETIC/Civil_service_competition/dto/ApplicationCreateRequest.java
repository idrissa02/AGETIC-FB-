package com.AGETIC.Civil_service_competition.dto;


    
import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ApplicationCreateRequest(
    
        @NotNull Long examId,
        @NotBlank String ninaNumber,
        @NotBlank String name,
        @NotBlank String surname,
        @Size(max = 50) String phone,
        @Email @NotBlank String email,
        LocalDate birthDate
) {}