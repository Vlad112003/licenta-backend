package com.elearning.backend.model.dto;

///
/// AnswerEvaluationRequest
/// params: None
/// return: None
/// description: This class represents a request for evaluating an answer.
public class AnswerEvaluationRequest {
    private String question;
    private String correctAnswer;
    private String studentAnswer;

    // Getters și Setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }

    public String getCorrectAnswer() { return correctAnswer; }
    public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

    public String getStudentAnswer() { return studentAnswer; }
    public void setStudentAnswer(String studentAnswer) { this.studentAnswer = studentAnswer;
    }
}

