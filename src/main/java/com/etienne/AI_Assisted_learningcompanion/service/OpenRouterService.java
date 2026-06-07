package com.etienne.AI_Assisted_learningcompanion.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class OpenRouterService {

    @Value("${openrouter.api.key}")
    private String apiKey;

    @Value("${openrouter.model}")
    private String model;

    public String askOpenRouter(String prompt) {

        try {
            URL url = new URL("https://openrouter.ai/api/v1/chat/completions");

            HttpURLConnection connection =
                    (HttpURLConnection) url.openConnection();

            connection.setConnectTimeout(8000);
            connection.setReadTimeout(20000);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("HTTP-Referer", "http://localhost:8080");
            connection.setRequestProperty("X-Title", "AI Assisted Learning Companion");
            connection.setDoOutput(true);

            String safePrompt = """
                    You are a professional AI learning assistant.

                    Important rules:
                    - Return text only.
                    - Do not generate images.
                    - Do not return base64.
                    - Do not return raw JSON.
                    - Do not use markdown tables.
                    - Keep the response clear, short and educational.
                    - Maximum answer length: 250 words.

                    Student request:
                    """ + prompt;

            String body = """
                    {
                      "model": "%s",
                      "max_tokens": 600,
                      "temperature": 0.4,
                      "messages": [
                        {
                          "role": "user",
                          "content": %s
                        }
                      ]
                    }
                    """.formatted(
                    model,
                    toJsonString(safePrompt)
            );

            try (OutputStream os = connection.getOutputStream()) {
                os.write(body.getBytes(StandardCharsets.UTF_8));
                os.flush();
            }

            int statusCode = connection.getResponseCode();

            InputStream responseStream =
                    statusCode >= 400
                            ? connection.getErrorStream()
                            : connection.getInputStream();

            String rawResponse = readStream(responseStream);

            if (statusCode >= 400) {
                return "OpenRouter Error " + statusCode + ": " + rawResponse;
            }

            String answer = extractContent(rawResponse);

            if (answer == null || answer.isBlank()) {
                return "The AI did not return a readable text response. Please try again.";
            }

            if (answer.contains("data:image")
                    || answer.contains("base64")
                    || answer.length() > 3000) {

                return "The AI returned an unsupported response. Please ask again using a text-based question.";
            }

            return answer.trim();

        } catch (java.net.SocketTimeoutException e) {

            return "The AI is taking too long to respond. Please try again with a shorter question.";

        } catch (Exception e) {

            e.printStackTrace();

            return "OpenRouter AI Error: " + e.getMessage();
        }
    }

    private String readStream(InputStream stream) throws IOException {

        if (stream == null) {
            return "";
        }

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(stream, StandardCharsets.UTF_8)
                );

        StringBuilder response = new StringBuilder();

        String line;

        while ((line = reader.readLine()) != null) {
            response.append(line);
        }

        reader.close();

        return response.toString();
    }

    private String toJsonString(String value) {

        if (value == null) {
            value = "";
        }

        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }

    private String extractContent(String json) {

        String marker = "\"content\":\"";

        int start = json.indexOf(marker);

        if (start < 0) {
            return "";
        }

        start += marker.length();

        StringBuilder content = new StringBuilder();

        boolean escaping = false;

        for (int i = start; i < json.length(); i++) {

            char c = json.charAt(i);

            if (escaping) {

                if (c == 'n') {
                    content.append('\n');
                } else if (c == 'r') {
                    content.append('\r');
                } else if (c == 't') {
                    content.append('\t');
                } else if (c == '"') {
                    content.append('"');
                } else if (c == '\\') {
                    content.append('\\');
                } else {
                    content.append(c);
                }

                escaping = false;

            } else if (c == '\\') {

                escaping = true;

            } else if (c == '"') {

                break;

            } else {

                content.append(c);
            }
        }

        return content.toString();
    }
}