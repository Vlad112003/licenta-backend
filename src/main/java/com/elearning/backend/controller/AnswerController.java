package com.elearning.backend.controller;

import com.elearning.backend.service.AnswerGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerGenerationService answerGenerationService;

    ///
    /// Constructor for AnswerController
    /// params: AnswerGenerationService answerGenerationService
    /// return: None
    /// description: This constructor initializes the AnswerController with the provided AnswerGenerationService.
    @Autowired
    public AnswerController(AnswerGenerationService answerGenerationService) {
        this.answerGenerationService = answerGenerationService;
    }

    ///
    /// Generates answers for a list of questions based on provided lesson content
    /// params: Map<String, Object> requestBody containing questions and lessonContent
    /// return: String
    /// description: This method generates answers for the provided list of questions using the lesson content
    @PostMapping("/generate")
    public ResponseEntity<String> generateAnswers(@RequestBody Map<String, Object> requestBody) {
        List<String> questions = (List<String>) requestBody.get("questions");
        String lessonContent = (String) requestBody.get("lessonContent");

        if (questions == null || lessonContent == null) {
            return ResponseEntity.badRequest().body("Both questions and lessonContent are required");
        }

        return ResponseEntity.ok(answerGenerationService.generateAnswers(questions, lessonContent));
    }
}