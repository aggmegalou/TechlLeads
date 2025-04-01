// /*
//  * Copyright [2024-2025] [TechLeads]
//  *
//  * Licensed under multiple licenses:
//  * 1. Apache License, Version 2.0 (the «Apache License»);
//  *    You may obtain a copy of the Apache License at:
//  *        http://www.apache.org/licenses/LICENSE-2.0
//  *
//  * 2. MIT License (the «MIT License»);
//  *    You may obtain a copy of the MIT License at:
//  *        https://opensource.org/licenses/MIT
//  *
//  * 3. Eclipse Public License 2.0 (the «EPL 2.0»);
//  *    You may obtain a copy of the EPL 2.0 at:
//  *        https://www.eclipse.org/legal/epl-2.0/
//  *
//  * You may not use this file except in compliance with one or more of these licenses.
//  * Unless required by applicable law or agreed to in writing, software distributed
//  * under these licenses is distributed on an «AS IS» BASIS, WITHOUT WARRANTIES
//  * OR CONDITIONS OF ANY KIND, either express or implied.
//  * See the applicable licenses for the specific language governing permissions and
//  * limitations under those licenses.
//  */

package com.example.demo.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.example.demo.openai.agents.ReviewerResearcher;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * This class represents my class in Java.
 * 
 * @author Valeria Soumelidi
 * @version 1.0
 */

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class ReviewerResearcherTest {

    @Spy
    @InjectMocks
    private ReviewerResearcher reviewerResearcher;

    @BeforeAll
    void setUp() throws IOException {
        ReflectionTestUtils.setField(reviewerResearcher, "instructions", ReviewerResearcher.INSTRUCTIONS);
        ReflectionTestUtils.setField(reviewerResearcher, "name", ReviewerResearcher.NAME);
        ReflectionTestUtils.setField(reviewerResearcher, "model", ReviewerResearcher.MODEL);
    }

    private static void mockHttpClient(ReviewerResearcher runtimeClass, final String serializedBody) throws IOException {
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200).message("").body(
                        ResponseBody.create(
                                serializedBody,
                                MediaType.parse("application/json")))
                .build();
        doReturn(CompletableFuture.completedFuture(response)).when(runtimeClass).sendRequest(anyString(), anyString());
    }

    @Test
    void createAssistantTest() throws IOException, InterruptedException, ExecutionException {

        mockHttpClient(reviewerResearcher, "{\"id\": \"assistant-id-123\"}");

        assertEquals(reviewerResearcher.getModel(), ReviewerResearcher.MODEL);
        assertEquals(reviewerResearcher.getInstructions(), ReviewerResearcher.INSTRUCTIONS);
        assertEquals(reviewerResearcher.getName(), ReviewerResearcher.NAME);
        CompletableFuture<String> future = reviewerResearcher.createAiAssistant();
        future.join();
        assertEquals("assistant-id-123", future.get());
    }

    @Test
    void testCreateAiAssistant() throws IOException, JSONException, InterruptedException, ExecutionException {
        // Simulate successful response
        // when(mockResponse.isSuccessful()).thenReturn(true);
        String mockResponseBody = "{\"id\":\"assistant-id-123\"}";
        mockHttpClient(reviewerResearcher, mockResponseBody);

        assertEquals(reviewerResearcher.loadKey(), "${OPENAI_API_KEY}");
        assertEquals(reviewerResearcher.getName(), ReviewerResearcher.NAME);
        CompletableFuture<String> response = reviewerResearcher.createAiAssistant();
        assertEquals("assistant-id-123", response.join());
    }

    @Test
    void loadKeyTest() {
        assertNotNull(reviewerResearcher.loadKey(), "The key must not be null");
    }

    @Test
    void testBuildJsonForAssistant() {
        // Generate JSON and verify its structure
        String json = reviewerResearcher.buildJsonForAssistant();
        JSONObject jsonObject = new JSONObject(json);

        assertEquals("gpt-4o-mini", jsonObject.getString("model"));
        assertEquals(ReviewerResearcher.INSTRUCTIONS,
                jsonObject.getString("instructions"));
        assertEquals(ReviewerResearcher.NAME, jsonObject.getString("name"));
        assertTrue(!jsonObject.has("tools"));
    }

    @Test
    void testSendRequestSuccess() throws IOException {
    
        String jsonRequest = reviewerResearcher.buildJsonForAssistant();
        String url = "https://api.openai.com/v1/assistants";
        CompletableFuture<Response> response = reviewerResearcher.sendRequest(jsonRequest, url);

        assertNotNull(response);
    }

    @Test
    void testExtractId() throws IOException {
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200).message("").body(
                        ResponseBody.create(
                                "{\"id\":\"assistant-id-123\"}",
                                MediaType.parse("application/json")))
                .build();

        String id = reviewerResearcher.extractId(response);

        assertEquals("assistant-id-123", id);
    }

    @AfterAll
    void deleteAssistant() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/assistants/" + reviewerResearcher.getAssistantId())
                .delete()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + reviewerResearcher.loadKey())
                .addHeader("OpenAI-Beta", "assistants=v2")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            System.out.println("The delete of assistant is unable");
    }
}
