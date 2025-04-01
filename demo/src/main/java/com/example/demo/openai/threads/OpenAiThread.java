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
package com.example.demo.openai.threads;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
 * 
 * @author Anna Maria Megalou
 * @version 1.0
 */

@Service("OpenAiThread")
public class OpenAiThread {

    private String instructions;
    private String threadId = "";

    @Value("${openai.api.key}")
    private String key;

    protected String assistantId;

    // send request to assistant API
    @Async
    public CompletableFuture<Response> sendRequest(String jsonRequest, String url) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

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

    // extract the id from JsonObject
    public String extractId(Response response) throws JSONException, IOException {
        JSONObject jsonResponse = new JSONObject(response.body().string());
        return jsonResponse.getString("id");
    }

    @Async
    public CompletableFuture<String> createThread(String instructions, String assistantId)
            throws IOException, JSONException, InterruptedException, ExecutionException {
        this.instructions = instructions;
        this.assistantId = assistantId;

        String jsonRequest = "";
        CompletableFuture<Response> response = sendRequest(jsonRequest, "https://api.openai.com/v1/threads");
        CompletableFuture.allOf(response).join();

        if (response.get() != null && response.get().isSuccessful()) {
            threadId = extractId(response.get());
            System.out.println("Thread id is created successfully. ID: " + getThreadId());
            return CompletableFuture.completedFuture(threadId);
        } else {
            System.out.println("The creation of thread is unable");
            throw new IOException();
        }
    }

    // Adding message to the assistant
    @Async
    public CompletableFuture<String> addMessage(String role, String message, String thread)
            throws IOException, InterruptedException, ExecutionException {

        JSONObject jsonObject = new JSONObject()
                .put("role", role)
                .put("content", message);

        String jsonRequest = jsonObject.toString();

        CompletableFuture<Response> response = sendRequest(jsonRequest,
                "https://api.openai.com/v1/threads/" + thread + "/messages");
        CompletableFuture.allOf(response).join();
        if (response.get().isSuccessful() && response.get().body() != null) {
            System.out.println("Message add message successfully.");
            return CompletableFuture.completedFuture(response.get().body().string());
        } else {
            System.err.println("Failed to add message ");
            System.err.println(response.get().body().string());
            throw new Error();
        }

    }

    @Async
    public CompletableFuture<String> run() throws IOException, InterruptedException, ExecutionException {

        JSONObject jsonObject = new JSONObject()
                .put("assistant_id", getAssistantId())
                .put("stream", true);

        String jsonRequest = jsonObject.toString();

        CompletableFuture<Response> response = sendRequest(jsonRequest,
                "https://api.openai.com/v1/threads/" + getThreadId() + "/runs");
        CompletableFuture.allOf(response).join();

        if (response.get().isSuccessful() && response.get().body() != null) {
            System.out.println("Message sent successfully to user.");
            return CompletableFuture.completedFuture(response.get().body().string());
        } else {
            System.out.println("Failed to get response " + response.get().body().string());
            throw new IOException();
        }

    }

    // get Answer from assistant
    @Async
    public CompletableFuture<String> getRequest() throws IOException, InterruptedException, ExecutionException {

        CompletableFuture<Response> response = getrequest();
        CompletableFuture.allOf(response).join();

        if (response.get().isSuccessful() && response.get().body() != null) {
            String responseBody = response.get().body().string();
            JSONObject jsonResponse = new JSONObject(responseBody);
            String assistantsResult = jsonResponse.getJSONArray("data").getJSONObject(0)
                    .getJSONArray("content").getJSONObject(0).getJSONObject("text")
                    .getString("value");
            return CompletableFuture.completedFuture(assistantsResult);

        } else {
            System.out.println("Failed to get the result");
            System.err.println(response.get().body().string());
            throw new Error();
        }
    }

    public CompletableFuture<Response> getrequest() throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/threads/" + getThreadId() + "/messages")
                .get()
                .addHeader("Authorization", "Bearer " + key)
                .addHeader("Content-Type", "application/json")
                .addHeader("OpenAI-Beta", "assistants=v2")
                .build();

        return CompletableFuture.completedFuture(client.newCall(request).execute());
    }

    // some getters and setters for private fields
    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getThreadId() {
        return threadId;
    }

    public String getKey() {
        return key;
    }

    public String getAssistantId() {
        return assistantId;
    }

}
