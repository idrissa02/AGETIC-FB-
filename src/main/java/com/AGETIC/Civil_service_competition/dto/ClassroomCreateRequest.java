package com.AGETIC.Civil_service_competition.dto;

public record ClassroomCreateRequest(
    String number,
    int capacity,
    Long centerId
) {}
