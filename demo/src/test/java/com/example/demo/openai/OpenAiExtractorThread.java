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

/**
* This class represents my class in Java.
* @author Anna Maria Megalou
* @version 1.0
*/
package com.example.demo.openai;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


import org.json.JSONException;
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

import com.example.demo.openai.agents.OpenAiAssistant;
import com.example.demo.openai.threads.ExtractorThread;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

@ExtendWith(SpringExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
public class OpenAiExtractorThread {

    @Spy
    @InjectMocks
    private ExtractorThread extractorThread;

    @Spy
    @InjectMocks
    private OpenAiAssistant openAiAssistant;

    public static final String INSTRUCTIONS = "You are responsible for a procedure of cv ranking where other agents are part of as well. Your role is to receive a job description and turn it in csv format (return it in text form)";
    public static final String AssistantId = "assistant-id-123";
    public static final String MODEL = "gpt-4o-mini";
    public static final String NAME = "Register";

    @BeforeAll
    void setUp() throws IOException {
        ReflectionTestUtils.setField(extractorThread, "instructions", INSTRUCTIONS);
        ReflectionTestUtils.setField(extractorThread, "assistantId", AssistantId);
        ReflectionTestUtils.setField(openAiAssistant, "instructions", INSTRUCTIONS);
        ReflectionTestUtils.setField(openAiAssistant, "name", NAME);
        ReflectionTestUtils.setField(openAiAssistant, "model", MODEL);
        ReflectionTestUtils.setField(openAiAssistant, "assistantId", "assistant-id-123");
        ReflectionTestUtils.setField(extractorThread, "threadId", "thread-id-123");

    }
    // private static void mockHttpClientUpload(ExtractorThread runtimeClass, final String serializedBody) throws IOException {
    //     Response response = new Response.Builder()
    //             .request(new Request.Builder().url("http://url.com").build())
    //             .protocol(Protocol.HTTP_1_1)
    //             .code(200).message("").body(
    //                     ResponseBody.create(
    //                             serializedBody,
    //                             MediaType.parse("application/json")))
    //             .build();
    //     doReturn(CompletableFuture.completedFuture(response)).when(runtimeClass).uploadFile(anyString(), any());
    // }

    private static void mockHttpClient(ExtractorThread runtimeClass, final String serializedBody) throws IOException {
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

    private static void mockHttpClient(OpenAiAssistant runtimeClass, final String serializedBody) throws IOException {
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

    private static void mockHttpClientget(ExtractorThread runtimeClass, final String serializedBody) throws IOException {
        Response response = new Response.Builder()
                .request(new Request.Builder().url("http://url.com").build())
                .protocol(Protocol.HTTP_1_1)
                .code(200).message("").body(
                        ResponseBody.create(
                                serializedBody,
                                MediaType.parse("application/json")))
                .build();
        doReturn(CompletableFuture.completedFuture(response)).when(runtimeClass).getrequest();
    }

    @Test
    void loadKeyTest() {
        assertNotNull(openAiAssistant.loadKey(), "The key must not be null");
    }

    @Test
    void testSendRequest_Success() throws IOException {
        String url = "https://api.openai.com/v1/threads";
        mockHttpClient(extractorThread, "{\"id\": \"assistant-id-123\"}");
        CompletableFuture<Response> response = extractorThread.sendRequest("{\"id\": \"assistant-id-123\"}", url);
        assertNotNull(response);
    }

    @Test
    void buildThreadTest() throws IOException, JSONException, InterruptedException, ExecutionException {
        mockHttpClient(extractorThread, "{\"id\": \"assistant-id-123\"}");

        assertNotNull(extractorThread.createThread("You are a german translator", "assistant-id-123"),
                "Thread ID should not be null after creation.");
        System.out.println("Thread created successfully. ID: " + extractorThread.getThreadId());
    }

    @Test
    void testAddMessage() throws IOException, InterruptedException, ExecutionException {
        mockHttpClient(extractorThread, "{\"id\": \"assistant-id-123\"}");
        extractorThread.getThreadId();
        assertNotNull(extractorThread.getThreadId(), "Thread ID should not be null after creation.");

        extractorThread.addMessage("user", "Tell me hi in german", extractorThread.getThreadId());
        System.out.println("Message added successfully to thread ID: " + extractorThread.getThreadId());
    }

    @Test
    void testRun() throws IOException, InterruptedException, ExecutionException {

        mockHttpClient(extractorThread, "{\"id\": \"thread-id-123\"}");
        mockHttpClient(openAiAssistant, "{\"id\": \"assistant-id-123\"}");

        assertNotNull(openAiAssistant.getAssistantId(), "Assistant ID should not be null after creation.");
        assertNotNull(extractorThread.getThreadId(), "Thread ID should not be null after creation.");

        extractorThread.addMessage("user", "Tell me hi in german", extractorThread.getThreadId());

        extractorThread.run();
        System.out.println("Run method executed successfully for thread ID: " + extractorThread.getThreadId());
    }

    // @Test
    // void upload() throws IOException, InterruptedException, ExecutionException {

    //     mockHttpClient(extractorThread, "{\"id\": \"thread-id-123\"}");
    //     mockHttpClientUpload(extractorThread, "{\"id\": \"file-id-123\"}");

    //     assertNotNull(extractorThread.getFileId(),"The file id should not be null");
    //     extractorThread.uploadFile("file name","ile-id-123".getBytes()).join();

    //     assertEquals("file-id-123", extractorThread.getFileId());
    // }

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

        String id = openAiAssistant.extractId(response);

        assertEquals("assistant-id-123", id);
    }

    @Test
    void testGetRequest() throws IOException, InterruptedException, ExecutionException {

        mockHttpClient(openAiAssistant, "{\"id\": \"assistant-id-123\"}");
        mockHttpClient(extractorThread, "{\"id\": \"thread-id-123\"}");

        assertNotNull(openAiAssistant.getAssistantId());
        assertNotNull(extractorThread.getThreadId(), "Thread ID should not be null after creation.");

        extractorThread.addMessage("user", "Tell me hi in german", extractorThread.getThreadId()).join();
        extractorThread.run().join();
        mockHttpClientget(extractorThread, "{\"data\": [{\"content\":[{\"text\":{\"value\":\"response text\"}}]}]}");

        extractorThread.getRequest();

    }

    @AfterAll
    void deleteAssistants() throws IOException {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/assistants/" + openAiAssistant.getAssistantId())
                .delete()
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + openAiAssistant.loadKey())
                .addHeader("OpenAI-Beta", "assistants=v2")
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful())
            System.out.println("The delete of assistant is unable");
    }

}
