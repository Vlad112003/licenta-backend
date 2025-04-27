package com.elearning.backend.controller;

import com.elearning.backend.service.QuestionGenerationService; // Import LlamaService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionGenerationService questionGenerationService; // Use LlamaService

    ///
    /// Constructor for QuestionController
    /// params: QuestionGenerationService questionGenerationService
    /// return: None
    /// description: This constructor initializes the QuestionController with the provided QuestionGenerationService.
    @Autowired
    public QuestionController(QuestionGenerationService questionGenerationService) {
        this.questionGenerationService = questionGenerationService; // Inject LlamaService
    }

    ///
    /// Generates questions from the provided text
    /// params: String text
    /// return: String
    /// description: This method generates questions from the provided text using the QuestionGenerationService.
    @PostMapping("/generate")
    public String generateQuestions(@RequestBody String text) {
        // Use LlamaService to generate questions from the provided text
        return questionGenerationService.generateQuestionsFromText(text); // Call the method to generate questions
    }
}
