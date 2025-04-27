package com.elearning.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.elearning.backend.model.dto.AnswerEvaluationRequest;

import java.util.*;

@Service
public class AnswerScoringService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    public String scoreAnswers(List<AnswerEvaluationRequest> evaluations) {
        StringBuilder result = new StringBuilder();

        for (AnswerEvaluationRequest eval : evaluations) {
            String score = evaluateSingle(eval.getQuestion(), eval.getCorrectAnswer(), eval.getStudentAnswer());

            result.append("Întrebare: ").append(eval.getQuestion()).append("\n");
            result.append("Răspuns elev: ").append(eval.getStudentAnswer()).append("\n");
            result.append("Scor: ").append(score).append("\n\n");
        }

        return result.toString();
    }

    private String evaluateSingle(String question, String correctAnswer, String studentAnswer) {
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "meta-llama/llama-4-maverick:free");

        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of(
                "role", "system",
                "content", "Ești un profesor care evaluează indulgent răspunsuri scurte la întrebări, chiar daca acestea" +
                        "sunt in limbaj natural (nu toti elevii vin din medii in care se vorbeste 100% corect) si nu te intereseaza diacriticele" +
                        ". Returnează doar un scor NUMERIC de la 0 la 100, fără explicații.\n\n" +
                        "- Returnează 100 dacă răspunsul atinge toate punctele cheie.\n" +
                        "- Returnează 0 dacă si numai daca nu există niciun răspuns (e gol).\n" +
                        "- Returnează 5 dacă si numai data este complet greșit, dar există text, nu conteaza ce este in acel text cat timp exista.\n" +
                        "- Returnează între 10 și 90 dacă răspunsul este parțial corect."
        ));

        messages.add(Map.of(
                "role", "user",
                "content", String.format("Întrebare: %s\nRăspuns corect: %s\nRăspuns elev: %s",
                        question, correctAnswer, studentAnswer)
        ));

        payload.put("messages", messages);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(API_URL, entity, Map.class);
            Map<?, ?> firstChoice = ((List<Map<?, ?>>) response.getBody().get("choices")).get(0);
            Map<?, ?> message = (Map<?, ?>) firstChoice.get("message");

            return message.get("content").toString().trim();
        } catch (Exception e) {
            return "-1"; // fallback în caz de eroare
        }
    }
}
