// src/main/java/com/AGETIC/Civil_service_competition/dto/CenterForm.java
package com.AGETIC.Civil_service_competition.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CenterForm(
    Long id,                                 // null for create, non-null for edit
    @NotBlank @Size(max = 150) String name,
    @NotBlank @Size(max = 200) String location
) {}


