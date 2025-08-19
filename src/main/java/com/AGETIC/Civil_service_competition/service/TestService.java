// src/main/java/com/AGETIC/Civil_service_competition/service/TestService.java
package com.AGETIC.Civil_service_competition.service;

import com.AGETIC.Civil_service_competition.dto.TestRequest;
import com.AGETIC.Civil_service_competition.dto.TestResponse;
import java.util.List;

public interface TestService {
    List<TestResponse> getAllTests();
    TestResponse getTestById(Long id);
    TestResponse addTest(TestRequest request);
    TestResponse updateTest(Long id, TestRequest request);
    void deleteTest(Long id);

    List<TestResponse> searchByTitle(String titleLike);
    List<TestResponse> listByExam(Long examId);
    List<TestResponse> searchByExamAndTitle(Long examId, String titleLike);
}

