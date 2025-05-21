package com.elearning.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class QuestionGenerationService {

    private final RestTemplate restTemplate;

    @Value("${gemini.api.url}")
    private String apiUrl;

    @Value("${gemini.api.key}")
    private String apiKey;

    public QuestionGenerationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateQuestionsFromText(String text) {
        // Create request payload for Gemini API
        Map<String, Object> payload = new HashMap<>();

        // Create contents structure
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();

        // Create parts structure
        List<Map<String, Object>> parts = new ArrayList<>();
        Map<String, Object> part = new HashMap<>();

        // Add instruction and text content
        part.put("text", "Generate questions only based on the following text:\n\n" + text);
        parts.add(part);

        content.put("parts", parts);
        contents.add(content);

        payload.put("contents", contents);

        // Set up headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(payload, headers);

        try {
            // Gemini API doesn't use Bearer auth in header, but query param for key
            String url = apiUrl + "/models/gemini-2.0-flash:generateContent?key=" + apiKey;
            ResponseEntity<Map> response = restTemplate.postForEntity(url, entity, Map.class);

            Map<?, ?> responseBody = response.getBody();
            List<Map<?, ?>> candidates = (List<Map<?, ?>>) responseBody.get("candidates");
            Map<?, ?> candidate = candidates.get(0);
            Map<?, ?> contentMap = (Map<?, ?>) candidate.get("content");
            List<Map<?, ?>> responseParts = (List<Map<?, ?>>) contentMap.get("parts");

            return responseParts.get(0).get("text").toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error generating questions: " + e.getMessage();
        }
    }
}