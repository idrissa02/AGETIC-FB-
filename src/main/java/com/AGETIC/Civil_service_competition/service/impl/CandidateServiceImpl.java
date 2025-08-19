package com.AGETIC.Civil_service_competition.service.impl;

import com.AGETIC.Civil_service_competition.dto.CandidateProfileResponse;
import com.AGETIC.Civil_service_competition.dto.CandidateResponse;
import com.AGETIC.Civil_service_competition.dto.GradeResponse;
import com.AGETIC.Civil_service_competition.Enum.CandidateStatus;
import com.AGETIC.Civil_service_competition.Enum.ApplicationStatus;
import com.AGETIC.Civil_service_competition.model.Application;
import com.AGETIC.Civil_service_competition.model.Candidate;
import com.AGETIC.Civil_service_competition.model.Exam;
import com.AGETIC.Civil_service_competition.model.Grade;
import com.AGETIC.Civil_service_competition.repository.ApplicationRepository;
import com.AGETIC.Civil_service_competition.repository.CandidateRepository;
import com.AGETIC.Civil_service_competition.repository.GradeRepository;
import com.AGETIC.Civil_service_competition.service.CandidateService;

import jakarta.transaction.Transactional;

import java.util.List;

import org.springframework.stereotype.Service;

import com.AGETIC.Civil_service_competition.dto.TestMiniResponse;



@Service
@Transactional
public class CandidateServiceImpl implements CandidateService {

    private final CandidateRepository candidateRepo;
        private final ApplicationRepository applicationRepo;
    private final GradeRepository gradeRepo;


    public CandidateServiceImpl(CandidateRepository candidateRepo,
                                ApplicationRepository applicationRepo,
                                GradeRepository gradeRepo) {
        this.candidateRepo = candidateRepo;
        this.applicationRepo = applicationRepo;
        this.gradeRepo = gradeRepo;
    }



    @Override
    public CandidateResponse getByCandidateNumber(String candidateNumber) {
        return candidateRepo.findByCandidateNumber(candidateNumber)
                .map(this::toResponse).orElse(null);
    }

    @Override
    public double computeFinalScore(Long candidateId) {
        var grades = gradeRepo.findByCandidateId(candidateId); // inject GradeRepository here
        if (grades.isEmpty()) {
            return 0.0;
        }
        double avg = grades.stream().mapToDouble(Grade::getScore).average().orElse(0.0);
        // cache if you added finalScore
        candidateRepo.findById(candidateId).ifPresent(c -> c.setFinalScore(avg));
        return avg;
    }

    @Override
    public void autoUpdateStatus(Long candidateId) {
        double score = computeFinalScore(candidateId);
        candidateRepo.findById(candidateId).ifPresent(c -> {
            c.setStatus(score >= 60.0 ? CandidateStatus.ADMITTED : CandidateStatus.REFUSED);
        });
    }

    // --- VIEW 1: by applicationId ---
    // If ACCEPTED -> infos + candidateNumber + exam infos
    // If REFUSED/PENDING -> only infos (+ status). No grades/tests.
    @Override
    public CandidateProfileResponse viewByApplicationId(Long applicationId) {
        Application app = applicationRepo.findById(applicationId).orElse(null);
        if (app == null) return null;

        boolean accepted = "ACCEPTED".equals(app.getStatus().name());
        Exam exam = accepted ? app.getExam() : null;
        String candidateNumber = accepted && app.getCandidate() != null
                ? app.getCandidate().getCandidateNumber() : null;

        return new CandidateProfileResponse(
                app.getId(),
                app.getStatus().name(),
                candidateNumber,                         // only if accepted
                maskMiddle(app.getNinaNumber()),
                app.getName(),
                app.getSurname(),
                maskEmail(app.getEmail()),
                maskPhone(app.getPhone()),
                app.getBirthDate(),

                // exam info only when accepted
                accepted ? exam.getId() : null,
                accepted ? exam.getTitle() : null,
                accepted ? exam.getDate() : null,

                // no tests/grades/finalScore in this view
                List.of(),
                List.of(),
                null
        );
    }


    // --- VIEW 2: by candidateNumber ---
    // Only possible if results are announced (exam.resultsPublished == true).
    // Returns ALL infos + tests + grades + final score.
    @Override
    public CandidateProfileResponse viewByCandidateNumber(String candidateNumber) {
        Candidate cand = candidateRepo.findByCandidateNumber(candidateNumber).orElse(null);
        if (cand == null) return null;

        Application app = cand.getApplication();
        Exam exam = app.getExam();

        // if you enforce “only possible if announced”:
        if (!exam.isResultsPublished()) {
            return null; // or throw new RuntimeException("Results not published yet");
        }

        var gradeList = gradeRepo.findByCandidateId(cand.getId());
        var grades = gradeList.stream()
                .map(g -> new GradeResponse(
                        g.getCandidate().getId(),
                        g.getTest().getId(),
                        g.getScore(),
                        g.getGradedAt(),
                        g.getUpdatedAt(),
                        g.getCorrectionReason(),
                        g.getGradedBy() != null ? g.getGradedBy().getId() : null
                )).toList();

        Double finalScore = gradeList.isEmpty()
                ? null
                : gradeList.stream().mapToDouble(Grade::getScore).average().orElse(0.0);

        var tests = exam.getTests().stream()
                .map(t -> new TestMiniResponse(t.getId(), t.getTitle()))
                .toList();

        return new CandidateProfileResponse(
                app.getId(),
                cand.getStatus().name(),
                cand.getCandidateNumber(),
                maskMiddle(app.getNinaNumber()),
                app.getName(),
                app.getSurname(),
                maskEmail(app.getEmail()),
                maskPhone(app.getPhone()),
                app.getBirthDate(),
                exam.getId(),
                exam.getTitle(),
                exam.getDate(),
                tests,
                grades,
                finalScore
        );
    }

    // ---- helpers
    private CandidateResponse toResponse(Candidate c) {
        return new CandidateResponse(
                c.getId(),
                c.getCandidateNumber(),
                c.getStatus().name(),
                c.getApplication() != null ? c.getApplication().getId() : null
        );
    }

      // ---- masking helpers ----
    private String maskMiddle(String v) {
        if (v == null || v.length() <= 4) return v;
        int keep = Math.min(2, v.length());
        int tail = Math.min(2, v.length() - keep);
        return v.substring(0, keep) + "*".repeat(Math.max(0, v.length() - keep - tail)) + v.substring(v.length() - tail);
    }
    private String maskEmail(String email) {
        if (email == null || !email.contains("@")) return email;
        var parts = email.split("@", 2);
        var local = parts[0];
        if (local.length() <= 2) return "**@" + parts[1];
        return local.substring(0, 2) + "**@" + parts[1];
    }
    private String maskPhone(String p) {
        if (p == null || p.length() < 3) return p;
        return p.substring(0, 2) + "*".repeat(p.length() - 4) + p.substring(p.length() - 2);
    }

}
