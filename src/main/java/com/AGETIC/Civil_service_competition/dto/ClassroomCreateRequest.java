package com.AGETIC.Civil_service_competition.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;

@Data
public class ClassroomCreateRequest {
    @NotBlank
    private String number;

    @Min(1)
    private Integer capacity;

    private Long centerId; // Reference to Center's id
}