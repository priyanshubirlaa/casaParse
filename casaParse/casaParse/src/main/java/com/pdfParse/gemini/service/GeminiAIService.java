package com.pdfParse.gemini.service;

import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class GeminiAIService {
	 private static final String GEMINI_API_KEY = "AIzaSyALUnjKTRzrctqD8wOQwccHdIKCPCZhR4g";
	    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + GEMINI_API_KEY;

	    public String extractDataFromText(String text) throws IOException {
	        OkHttpClient client = new OkHttpClient();

	        String jsonPayload = "{\"contents\": [{\"parts\": [{\"text\": \"Extract name, email, opening balance, closing balance from this text:\\n" + text + ". Return the result in JSON format like this: {\\\"name\\\": \\\"value\\\", \\\"email\\\": \\\"value\\\", \\\"openingBalance\\\": \\\"value\\\", \\\"closingBalance\\\": \\\"value\\\"}\"}]}]}";

	        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
	        Request request = new Request.Builder()
	                .url(GEMINI_API_URL)
	                .post(body)
	                .addHeader("Content-Type", "application/json")
	                .build();

	        try (Response response = client.newCall(request).execute()) {
	            if (!response.isSuccessful()) {
	                String errorBody = response.body().string();
	                throw new IOException("Unexpected response: " + response + ", error body: " + errorBody);
	            }
	            String responseBody = response.body().string();
	            JSONObject jsonResponse = new JSONObject(responseBody);
	            JSONArray candidates = jsonResponse.getJSONArray("candidates");
	            if (candidates.length() > 0) {
	                JSONObject candidate = candidates.getJSONObject(0);
	                JSONObject content = candidate.getJSONObject("content");
	                JSONArray parts = content.getJSONArray("parts");
	                if (parts.length() > 0) {
	                    String extractedText = parts.getJSONObject(0).getString("text");
	                    //Remove the ```json and ```
	                    extractedText = extractedText.replace("```json", "").replace("```", "").trim();
	                    try{
	                        JSONObject extractedJson = new JSONObject(extractedText);
	                        return extractedJson.toString();
	                    } catch (org.json.JSONException e){
	                        return "Error: Could not parse JSON from Gemini API response: " + extractedText;
	                    }
	                }
	            }
	            return "Error: Could not extract data from Gemini API response.";
	        }
	    }
}