package com.elearning.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.json.JSONArray;
import org.json.JSONObject;

@Service
public class QuestionGenerationService {

    private final RestTemplate restTemplate;

    @Value("${openrouter.api.url}")
    private String openRouterApiUrl;

    @Value("${openrouter.api.key}")
    private String apiKey;

    public QuestionGenerationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String generateQuestionsFromText(String text) {
        // Construct the JSON request body as per the cURL example
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "meta-llama/llama-4-maverick:free");

        // Create the messages array as per the cURL request
        JSONArray messages = new JSONArray();

        // Add a system message to instruct the model
        JSONObject systemMessage = new JSONObject();
        systemMessage.put("role", "system");
        systemMessage.put("content", "Generate questions only based on the provided text.");
        messages.put(systemMessage);


        JSONObject message = new JSONObject();
        message.put("role", "user");

        // Create the content array for the message
        JSONArray content = new JSONArray();

        // Add text message
        JSONObject textContent = new JSONObject();
        textContent.put("type", "text");
        textContent.put("text", text);
        content.put(textContent);

        // Add the content to the message
        message.put("content", content);

        // Add message to messages array
        messages.put(message);

        // Attach the messages array to the request body
        requestBody.put("messages", messages);

        // Set up the headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey); // Add your API key here

        // Create the HttpEntity with the request body and headers
        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        // Send the POST request to OpenRouter API and get the response
        ResponseEntity<String> response = restTemplate.exchange(
                openRouterApiUrl, HttpMethod.POST, entity, String.class);

        // Return the generated response
        return response.getBody();
    }
}
