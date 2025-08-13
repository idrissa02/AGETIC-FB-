package com.AGETIC.Civil_service_competition.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CenterCreateRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String location;
}