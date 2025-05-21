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

    ///
    /// Generates answers for a list of questions based on provided lesson content
    /// params: List<String> questions, String lessonContent
    /// return: String
    /// description: This method generates answers for the provided list of questions using only the information in the lesson content
    public String generateAnswers(List<String> questions, String lessonContent) {
        StringBuilder answers = new StringBuilder();

        // Prepare a single request for all questions
        Map<String, Object> payload = new HashMap<>();
        payload.put("model", "meta-llama/llama-4-maverick:free");

        List<Map<String, String>> messages = new ArrayList<>();

        // System prompt instructs to use ONLY the provided content
        messages.add(Map.of(
                "role", "system",
                "content", "Ești un profesor care oferă răspunsuri la întrebări. " +
                        "Folosește DOAR informațiile din textul lecției furnizat. " +
                        "Nu adăuga informații externe sau cunoștințe generale. " +
                        "Dacă informația nu există în text, menționează că nu se află în conținutul lecției."
        ));

        // Format all questions with the lesson content
        StringBuilder userContent = new StringBuilder();
        userContent.append("Conținutul lecției:\n").append(lessonContent).append("\n\n");
        userContent.append("Răspunde la următoarele întrebări folosind DOAR informațiile din textul de mai sus:\n\n");

        for (String question : questions) {
            userContent.append("Întrebare: ").append(question).append("\n");
        }

        messages.add(Map.of(
                "role", "user",
                "content", userContent.toString()
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

            String content = message.get("content").toString().trim();

            // Process the response to format it correctly
            if (content.contains("Întrebare:")) {
                return content; // Already in expected format
            } else {
                // Parse and reformat the response
                String[] lines = content.split("\n");
                StringBuilder formattedResponse = new StringBuilder();

                for (int i = 0; i < questions.size(); i++) {
                    formattedResponse.append("Întrebare: ").append(questions.get(i)).append("\n");

                    if (i < lines.length) {
                        String answer = lines[i].trim();
                        // Remove any numbering at the beginning
                        answer = answer.replaceAll("^\\d+\\.\\s*", "");
                        // Remove "Răspuns:" prefix if it exists
                        answer = answer.replaceAll("^Răspuns:\\s*", "");

                        formattedResponse.append("Răspuns: ").append(answer).append("\n\n");
                    } else {
                        formattedResponse.append("Răspuns: Nu s-a putut genera un răspuns din conținutul lecției.\n\n");
                    }
                }

                return formattedResponse.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Fallback response if the API call fails
            StringBuilder errorResponse = new StringBuilder();
            for (String question : questions) {
                errorResponse.append("Întrebare: ").append(question).append("\n");
                errorResponse.append("Răspuns: Nu s-a putut genera un răspuns din conținutul lecției.\n\n");
            }
            return errorResponse.toString();
        }
    }
}