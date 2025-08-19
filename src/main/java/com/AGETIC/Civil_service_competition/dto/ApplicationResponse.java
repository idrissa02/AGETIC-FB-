package com.AGETIC.Civil_service_competition.dto;



import java.time.LocalDate;
import java.time.LocalDateTime;

public record ApplicationResponse(
        
        Long id,
        Long examId,
        String ninaNumber,
        String name,
        String surname,
        String phone,
        String email,
        String status,
        LocalDate birthDate,
        String pictureUrl,
        String documentsUrl,
        LocalDateTime createdAt
        
) {}