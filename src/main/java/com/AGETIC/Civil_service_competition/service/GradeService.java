package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.GradeCreateRequest;
import com.AGETIC.Civil_service_competition.dto.GradeResponse;

import java.util.List;

public interface GradeService {

    GradeResponse assignGrade(GradeCreateRequest request);

    List<GradeResponse> getGradesForCandidate(Long candidateId);

    List<GradeResponse> getGradesForTest(Long testId);

    GradeResponse updateGrade(GradeCreateRequest request);

    void deleteGrade(Long candidateId, Long testId);
}
