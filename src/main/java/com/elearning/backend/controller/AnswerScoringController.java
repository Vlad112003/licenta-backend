package com.elearning.backend.controller;

import com.elearning.backend.service.AnswerScoringService;
import com.elearning.backend.model.dto.AnswerEvaluationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/evaluation")
public class AnswerScoringController {

    private final AnswerScoringService scoringService;

    ///
    /// Constructor for AnswerScoringController
    /// params: AnswerScoringService scoringService
    /// return: None
    /// description: This constructor initializes the AnswerScoringController with the provided AnswerScoringService.
    @Autowired
    public AnswerScoringController(AnswerScoringService scoringService) {
        this.scoringService = scoringService;
    }

    ///
    /// Evaluates answers
    /// params: List<AnswerEvaluationRequest> answers
    /// return: String
    /// description: This method evaluates the provided answers using the AnswerScoringService.
    @PostMapping("/score")
    public String evaluateAnswers(@RequestBody List<AnswerEvaluationRequest> answers) {
        return scoringService.scoreAnswers(answers);
    }
}
