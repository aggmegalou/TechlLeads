/*
 * Copyright [2024-2025] [TechLeads]
 *
 * Licensed under multiple licenses:
 * 1. Apache License, Version 2.0 (the «Apache License»);
 *    You may obtain a copy of the Apache License at:
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * 2. MIT License (the «MIT License»);
 *    You may obtain a copy of the MIT License at:
 *        https://opensource.org/licenses/MIT
 *
 * 3. Eclipse Public License 2.0 (the «EPL 2.0»);
 *    You may obtain a copy of the EPL 2.0 at:
 *        https://www.eclipse.org/legal/epl-2.0/
 *
 * You may not use this file except in compliance with one or more of these licenses.
 * Unless required by applicable law or agreed to in writing, software distributed
 * under these licenses is distributed on an «AS IS» BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND, either express or implied.
 * See the applicable licenses for the specific language governing permissions and
 * limitations under those licenses.
 */
package com.example.demo.openai.agents;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * This class represents my class in Java.
 * @author Anna Maria Megalou
 * @version 1.0
 */

@Service
public class OpenAiAssistant {

    protected String model;
    protected String instructions;
    protected String name;
    private String assistantId;
    
    @Value("${openai.api.key}")
    private String key;


    // creatAssistant
    @Async
    public CompletableFuture <String> createAiAssistant() throws IOException, JSONException, InterruptedException, ExecutionException {
        String jsonRequest = buildJsonForAssistant();
        CompletableFuture<Response> response = sendRequest(jsonRequest, "https://api.openai.com/v1/assistants");

        CompletableFuture.allOf(response).join();
        
        if (response != null && response.get().isSuccessful()) {
            assistantId = extractId(response.get());
            System.out.println("Assistant created successfully. ID: " + getAssistantId());
            return CompletableFuture.completedFuture(assistantId);
        } else {
            System.out.println("The creation of assistant is unable");
            throw new IOException();
        }
    }

    // creating json object
    public String buildJsonForAssistant() {
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("instructions", getInstructions());
        jsonRequest.put("name", getName());
        jsonRequest.put("model", getModel());
        if (getModel().equals("gpt-4o")) {
            jsonRequest.put("tools", new JSONArray().put(new JSONObject().put("type", "file_search")));
        }
        return jsonRequest.toString();
    }

    // send request to assistant API
    @Async
    public CompletableFuture<Response> sendRequest(String jsonRequest, String url) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        @SuppressWarnings("deprecation")
        RequestBody body = RequestBody.create(mediaType, jsonRequest);

        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("OpenAI-Beta", "assistants=v2")
                .build();

        return CompletableFuture.completedFuture(client.newCall(request).execute());
    }

    // The method that load key from application.properties
    public String loadKey() {

        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
            return properties.getProperty("openai.api.key");
        } catch (IOException e) {
            System.err.print(e);
            return null;
        }
    }

    // extract the id from JsonObject
    public String extractId(Response response) throws JSONException, IOException {
        JSONObject jsonResponse = new JSONObject(response.body().string());
        return jsonResponse.getString("id");
    }

    public String getAssistantId() {
        return assistantId;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getName() {
        return name;
    }

    public String getModel() {
        return model;
    }
}
