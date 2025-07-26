package com.resume.builder.service;

import ch.qos.logback.core.joran.spi.HttpUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    private final WebClient webClient;
    public GeminiService(WebClient.Builder webClient) {
        this.webClient = webClient.build();
    }
    public String generate(String jd, String resume) {
        String prompt = buildPromt(jd, resume);
//        String prompt = buildPromt(emailRequest);
        Map<String, Object> requestBody = Map.of(
                "contents", new Object[] {
                        Map.of("parts", new Object[]{
                                Map.of("text", prompt)
                        })
                }
        );
//        HttpUtil webClient;
        String response = webClient.post()
                .uri("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent")
                .header("Content-Type", "application/json")
                .header("X-goog-api-key", "AIzaSyBACaG7TXaK6ChsHFP8XxApa5i_CfYJyI4")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractResponse(response);

    }

    private static String extractResponse(String response) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response);
            return rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text")
                    .asText();
        } catch (Exception e) {
            return "Error processing request: " + e.getMessage();
        }
    }

    private static String buildPromt(String jd, String resume) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Following is the JD:/n");
        prompt.append(jd);
        prompt.append("/nFollowing is the text extracted from my Resume:/n");
        prompt.append(resume);
        prompt.append("/nCalculate ATS score of resume based on JD. Also give suggestion for each section with a new text");
//        if(emailRequest.getTone()!=null && !emailRequest.getTone().isEmpty()){
//            prompt.append("Tone of the reply should be ").append(emailRequest.getTone());
//        }
//        prompt.append("/nOriginal email ").append(emailRequest.getEmailContent());
        return prompt.toString();
    }
}
