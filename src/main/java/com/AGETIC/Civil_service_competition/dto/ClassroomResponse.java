package com.AGETIC.Civil_service_competition.dto;

import lombok.Data;

@Data
public class ClassroomResponse {
    private Long id;
    private String number;
    private Integer capacity;
    private Long centerId;
    private String centerName;
}