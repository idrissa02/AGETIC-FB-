package com.AGETIC.Civil_service_competition.dto;

import java.util.List;

public record ExamCenterAssignRequest(Long examId, List<Long> centerIds) {}
