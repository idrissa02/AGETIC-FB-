package com.AGETIC.Civil_service_competition.controller;

import com.AGETIC.Civil_service_competition.dto.TestRequest;
import com.AGETIC.Civil_service_competition.dto.TestResponse;
import com.AGETIC.Civil_service_competition.service.TestService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
public class TestController {

    private final TestService service;

    public TestController(TestService service) {
        this.service = service;
    }

    /**
     * List tests.
     * - No params: all tests
     * - examId only: tests for an exam
     * - titleLike only: search by title (global)
     * - both examId + titleLike: search within an exam
     */
    @GetMapping
    public List<TestResponse> list(
            @RequestParam(required = false) Long examId,
            @RequestParam(required = false, name = "titleLike") String titleLike
    ) {
        if (examId != null && titleLike != null) {
            return service.searchByExamAndTitle(examId, titleLike);
        } else if (examId != null) {
            return service.listByExam(examId);
        } else if (titleLike != null) {
            return service.searchByTitle(titleLike);
        }
        return service.getAllTests();
    }

    @GetMapping("/{id}")
    public TestResponse getById(@PathVariable Long id) {
        return service.getTestById(id);
    }
      @GetMapping("/by-exam/{examId}")
    public List<TestResponse> listByExam(@PathVariable Long examId) {
        return service.listByExam(examId);
    } 

    @PostMapping
    public TestResponse create(@RequestBody @Valid TestRequest request) {
        return service.addTest(request);
    }

    @PutMapping("/{id}")
    public TestResponse update(@PathVariable Long id, @RequestBody @Valid TestRequest request) {
        return service.updateTest(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.deleteTest(id);
    }
}
