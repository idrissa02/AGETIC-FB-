// AdminExamUpdateRequest.java
package com.AGETIC.Civil_service_competition.dto;

import java.time.LocalDate;

public record AdminExamUpdateRequest(
    String title,
    LocalDate examDate,
    LocalDate applicationDeadline,
    String conditions,
    Integer quota,
    Integer hours,
    Long categoryId               
) {}

