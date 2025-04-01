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

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service("ExtractorThread")
public class ExtractorThread extends OpenAiThread {

    private String fileId = "";

    @Async
    public CompletableFuture<String> uploadFile(String filename, byte[] file) throws IOException {

        OkHttpClient client = new OkHttpClient().newBuilder().connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS).build();

        @SuppressWarnings("deprecation")
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("purpose", "assistants")
                .addFormDataPart("file",
                        filename,
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/files")
                .post(body)
                .addHeader("Authorization", "Bearer " + getKey())
                .build();
        Response response = client.newCall(request).execute();

        if (response.isSuccessful() && response.body() != null) {
            fileId = extractId(response);
        }
        return CompletableFuture.completedFuture(fileId);
    }

    @Override
    @Async
    public CompletableFuture<String> addMessage(String role, String message, String thread)
            throws IOException, InterruptedException, ExecutionException {
        JSONObject jsonObject = new JSONObject()
                .put("role", role)
                .put("content", message)
                .put("attachments",
                        new JSONArray().put(new JSONObject().put("file_id", getFileId()).put("tools",
                                new JSONArray().put(new JSONObject().put("type", "file_search")))));

        String jsonRequest = jsonObject.toString();

        CompletableFuture<Response> response = sendRequest(jsonRequest,
                "https://api.openai.com/v1/threads/" + thread + "/messages");

        CompletableFuture.allOf(response).join();

        if (response.get().isSuccessful() && response.get().body() != null) {
            System.out.println("Message add message successfully.");
            return CompletableFuture.completedFuture(response.get().body().string());
        } else {
            System.err.println("Failed to add message ");
            throw new Error();
        }
    }

    public String getFileId() {
        return fileId;
    }

}