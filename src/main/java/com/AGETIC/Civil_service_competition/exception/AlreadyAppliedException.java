package com.AGETIC.Civil_service_competition.exception;

public class AlreadyAppliedException extends RuntimeException {
    private final String ninaNumber;
    private final Long examId;

    public AlreadyAppliedException(String ninaNumber, Long examId) {
        super("Already applied to this exam");
        this.ninaNumber = ninaNumber;
        this.examId = examId;
    }

    public String getNinaNumber() { return ninaNumber; }
    public Long getExamId() { return examId; }
}

