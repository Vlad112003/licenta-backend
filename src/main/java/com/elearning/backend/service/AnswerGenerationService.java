package com.elearning.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AnswerGenerationService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    private final String API_URL = "https://openrouter.ai/api/v1/chat/completions";

    private final RestTemplate restTemplate = new RestTemplate();

    ///
    /// Constructor for AnswerGenerationService
    /// params: RestTemplate restTemplate
    /// return: None
    /// description: This constructor initializes the AnswerGenerationService with the provided RestTemplate. This class uses the OpenRouter API to generate answers to questions, by sending
    /// the questions to the API and receiving the generated answers.
    public String generateAnswers(List<String> questions) {
        StringBuilder answers = new StringBuilder();

        for (String question : questions) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("model", "meta-llama/llama-4-maverick:free");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of(
                    "role", "system",
                    "content", "Ești un profesor care oferă răspunsuri la întrebări. Fii clar, scurt și corect."
            ));
            messages.add(Map.of(
                    "role", "user",
                    "content", "Întrebare: " + question
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

                answers.append("Întrebare: ").append(question).append("\n");
                answers.append("Răspuns: ").append(message.get("content").toString().trim()).append("\n\n");

            } catch (Exception e) {
                answers.append("Eroare la întrebarea: ").append(question).append("\n");
            }
        }

        return answers.toString();
    }
}
