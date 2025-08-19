package com.AGETIC.Civil_service_competition.dto;

public record TestResponse(
    Long id,
    String title,
    Long examId,
    String examTitle,
    long gradesCount
) {}
