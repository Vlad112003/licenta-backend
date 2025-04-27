package com.elearning.backend.controller;

import com.elearning.backend.service.AnswerGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    /// Generates answers for a list of questions
    /// params: List<String> questions
    /// return: String
    /// description: This method generates answers for the provided list of questions using the AnswerGenerationService.
    @PostMapping("/generate")
    public String generateAnswers(@RequestBody List<String> questions) {
        return answerGenerationService.generateAnswers(questions);
    }
}
