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
package com.example.demo.openai.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.database.ranking.RankingResult;
import com.example.demo.database.ranking.RankingService;
import com.example.demo.database.researcher.ResearcherResult;
import com.example.demo.database.researcher.ResearcherService;
import com.example.demo.database.user.Users;
import com.example.demo.database.user.UsersService;
import com.example.demo.openai.agents.Extractor;
import com.example.demo.openai.agents.ExtractorResearcher;
import com.example.demo.openai.agents.OpenAiAssistant;
import com.example.demo.openai.agents.RankingAgent;
import com.example.demo.openai.agents.Register;
import com.example.demo.openai.agents.ReviewerRanking;
import com.example.demo.openai.agents.ReviewerResearcher;
import com.example.demo.openai.agents.SummaryAgent;
import com.example.demo.openai.threads.ExtractorThread;
import com.example.demo.openai.threads.OpenAiThread;

/**
 * This class represents my class in Java.
 * 
 * @author Aggeliki Despoina Megalou,
 * @author Anna Maria Megalou
 * @version 1.0
 */

@Service
public class OpenAiService {

    @Autowired
    public Register register;
    @Autowired
    public Extractor extractor;
    @Autowired
    public ExtractorResearcher extractorResearcher;
    @Autowired
    public ReviewerResearcher reviewerResearcher;
    @Autowired
    public ReviewerRanking reviewerRanking;
    @Autowired
    public RankingAgent rankingAgent;
    @Autowired
    public SummaryAgent summaryAgent;

    @Autowired
    @Qualifier(value = "OpenAiThread")
    public OpenAiThread registerOpenAiThread;
    @Autowired
    @Qualifier(value = "ExtractorThread")
    public ExtractorThread extractorOpenAiThread;
    @Autowired
    @Qualifier(value = "OpenAiThread")
    public OpenAiThread extractorResearcherOpenAiThread;
    @Autowired
    @Qualifier(value = "OpenAiThread")
    public OpenAiThread reviewerResearcherThread;
    @Autowired
    @Qualifier(value = "OpenAiThread")
    public OpenAiThread reviewerRankingThread;
    @Autowired
    @Qualifier(value = "OpenAiThread")
    public OpenAiThread rankingAgentThread;
    @Autowired
    @Qualifier(value = "OpenAiThread")
    public OpenAiThread summaryThread;

    @Autowired
    UsersService usersService;

    @Autowired
    ResearcherService researcherService;

    @Autowired
    RankingService rankingService;

    ResearcherResult researcherResult;

    RankingResult rankingResult;

    String extractorResearcherThread = "";
    String rankingThread = "";

    HashMap<String, String> researcherHashMap;
    HashMap<String, String> rankingHashMap;

    // this method is called from controller class after the submit
    // It is execute all the ranking proccess
    @Async
    public CompletableFuture<String> startRankingProcess(HashMap<String, byte[]> files, Users user)
            throws Exception {

        String requestSessionId = UUID.randomUUID().toString();

        researcherHashMap = new HashMap<String, String>();
        rankingHashMap = new HashMap<String, String>();

        // Step 1:create register
        String messageRegiser = "Here are the details provided by the user:\nfield:" + user.getField() +
                "\nhard skills:" + user.getHardSkills() +
                "\nsoft skills:" + user.getSoftSkills() +
                "\n other tratits:" + user.getOtherTraits();

        CompletableFuture<String> registerResponse = registerResponse(messageRegiser);
        System.out.println("Register Response: " + registerResponse.get());
        CompletableFuture.allOf(registerResponse).join();

        for (String file : files.keySet()) {

            // step 2:create extractor
            String messageExtractor = "The cv that i want you to extract informations is the following: ";
            CompletableFuture<String> extractorResponse = extractorResponse(messageExtractor, file, files.get(file));
            CompletableFuture.allOf(extractorResponse).join();
            System.out.println("Extractor Response: " + extractorResponse.get());

            // step 3:Create extractorResearcher
            String extractorResearcherMessage = "The resume in csv is:" + extractorResponse.get()
                    + "/n The job position in csv is:" + registerResponse.get();
            CompletableFuture<String> extractorResearcherResponse = extractorResearcherResponse(
                    extractorResearcherMessage);
            CompletableFuture.allOf(extractorResearcherResponse).join();
            System.out.println("ExtractorResearcher response is :" + extractorResearcherResponse.get());
            String extractorResearcher = extractorResearcherResponse.get();

            // step 4:Create reviewer reasearcher
            String messageResearcherReviewer = "The requiremnts are:\n" + registerResponse.get()
                    + "\nThe pdf contentns are:\n " + extractorResponse.get()
                    + "\nThe response of extractorReasearcher I WANT TO YOU TO REVIEW BASED ON THE PREVIOUS FACTS is: "
                    + extractorResearcherResponse.get();
            CompletableFuture<String> reviewerExtractorResponse = reviewerExtractorResponse(messageResearcherReviewer);
            CompletableFuture.allOf(reviewerExtractorResponse).join();
            String reviewerResponse = reviewerExtractorResponse.get();

            // check if the extractorresearcher gave the willing results
            if (!reviewerResponse.contains("---- NO CHANGES REQUIRED, ANALYSIS GOOD ----")) {
                CompletableFuture<String> correctExtractor = checkReviewerResearcherResult(extractorResearcher,
                        reviewerResponse);
                CompletableFuture.allOf(correctExtractor).join();
                extractorResearcher = correctExtractor.get();

            }
            String summaryMessage = "The csv that i want to extract summary in text based in suitability of applicant for the specific job position is :"
                    + extractorResearcher
                    + "I dont want the summary in csv format but want to return a small text(paragraph)";
            CompletableFuture<String> summary = summaryResponse(summaryMessage);
            CompletableFuture.allOf(summary).join();
            System.out.println(summary.get());

            researcherResult = new ResearcherResult();
            researcherResult.setResume(summary.get());
            researcherResult.setFileName(file);
            researcherResult.setSessionId(requestSessionId);
            researcherService.saveResearcherResult(researcherResult);

            // file name, extractor response
            researcherHashMap.put(file, extractorResearcherResponse.get());
        }

        System.out.println(researcherHashMap);
        // get the columns content from database
        List<ResearcherResult> databaseData = researcherService.getAllresearcher(requestSessionId);
        System.out.println(databaseData);

        String messageRanking = " The resume and the id of every resume from the database are:\n";

        for (ResearcherResult dbData : databaseData) {
            messageRanking += dbData.getFileName() + ": " + dbData.getResume() + "\n";
        }

        messageRanking = messageRanking +
                "\nI want to ranking the resumes based on that job position csv: " +
                registerResponse.get() +
                "I WANT TO RETURN ME A CSV of fileNames(ONLY THE file names) FROM DATABASE SORTING. THAT MEANS IN THE FIRST POSITION OF CSV MUST BE THE file name of BEST RESUME AND IN THE LAST THE file name of  WORST resume . I want to return me only the csv and the csv to split the filename with , ";
        ;

        // step 5:create ranking
        CompletableFuture<String> rankingResponse = rankingAgentResponse(messageRanking);
        CompletableFuture.allOf(rankingResponse).join();
        String rankingresponse = rankingResponse.get();
        System.out.println("The response of ranking is: " + rankingResponse.get());

        String messageReviewerRanking = "The response of RankingAgent is:" + rankingResponse.get()
                + "Based on the following csv job position: " + registerResponse.get()
                + " And the following csv's of applicants cv's : " + databaseData
                + "I want to review the result of Ranking agent and return me what i have tell you in your instrucrions.";

        // step 6: Create reviewer ranking
        CompletableFuture<String> reviewerRankingResponse = reviewerRankingResponse(messageReviewerRanking);
        CompletableFuture.allOf(reviewerRankingResponse).join();
        System.out.println("The response of ranking is: " + reviewerRankingResponse.get());
        String responseOfReviewerRanking = reviewerRankingResponse.get();

        // Check if the ReviewerRanking gave the willing results
        if (!responseOfReviewerRanking.contains("---- NO CHANGES REQUIRED, ANALYSIS GOOD ----")) {
            rankingResponse = checkRankingReviewerResult(rankingresponse,
                    responseOfReviewerRanking);

        }

        // We split the answer of ranking in tokens
        StringTokenizer tokenizeRankingResponse = new StringTokenizer(
                rankingresponse, ",");

        // The tokens are saved in a list
        List<String> tokens = new ArrayList<>();
        while (tokenizeRankingResponse.hasMoreTokens()) {
            tokens.add(tokenizeRankingResponse.nextToken());
        }

        String sessionId = UUID.randomUUID().toString();

        // Check which file name match in every token
        // save every match in database and hashmap
        tokens.forEach((token) -> {
            researcherHashMap.keySet().forEach((fileName) -> {
                if (token.contains(fileName)) {
                    rankingHashMap.put(fileName, researcherHashMap.get(fileName));
                    rankingResult = new RankingResult();
                    rankingResult.setResume(fileName);
                    rankingResult.setSessionId(sessionId);
                    rankingResult.setResumeSummary(researcherHashMap.get(fileName));
                    rankingService.saveRankingResult(rankingResult);
                }
            });

        });

        System.out.println(rankingHashMap);

        return CompletableFuture.completedFuture(sessionId);
    }

    /*
     * executing the procedure of creating an assistant until getting assistant
     * response
     */
    @Async
    public CompletableFuture<String> processRequest(String message, String instructions, OpenAiAssistant assistant,
            OpenAiThread thread, boolean uploadFile, String filename, byte[] file) throws Exception {

        // Step 1: Create AI Assistant
        CompletableFuture<String> createAssistant = assistant.createAiAssistant();
        CompletableFuture.allOf(createAssistant).join();

        // Step 2: Create Thread
        CompletableFuture<String> createThread = thread.createThread(instructions, assistant.getAssistantId());
        CompletableFuture.allOf(createThread).join();
        if (assistant instanceof ExtractorResearcher) {
            extractorResearcherThread = createThread.get();
        } else if (assistant instanceof RankingAgent) {
            rankingThread = createThread.get();
        }

        // Step 3: Optional File Upload
        // the upload file must have an argument with the cv path or file
        if (uploadFile) {
            CompletableFuture<String> fileUpload = extractorOpenAiThread.uploadFile(filename, file);
            CompletableFuture.allOf(fileUpload).join();
        }

        // Step 4: Add Message
        CompletableFuture<String> addMessage = thread.addMessage("user", message, thread.getThreadId());
        CompletableFuture.allOf(addMessage).join();

        // Step 5: Run Thread
        CompletableFuture<String> runThread = thread.run();
        CompletableFuture.allOf(runThread).join();

        // Step 6: Get Response
        CompletableFuture<String> response = thread.getRequest();
        CompletableFuture.allOf(response).join();
        return CompletableFuture.completedFuture(response.get());
    }

    @Async
    public CompletableFuture<String> registerResponse(String messageRegister) throws Exception {
        return processRequest(messageRegister, Register.INSTRUCTIONS, register, registerOpenAiThread, false, null,
                null);
    }

    @Async
    public CompletableFuture<String> extractorResponse(String messageExtractor, String filename, byte[] file)
            throws Exception {
        return processRequest(messageExtractor, Extractor.INSTRUCTIONS, extractor, extractorOpenAiThread, true,
                filename, file);
    }

    @Async
    public CompletableFuture<String> extractorResearcherResponse(String researcherExtractor) throws Exception {
        return processRequest(researcherExtractor, ExtractorResearcher.INSTRUCTIONS, extractorResearcher,
                extractorResearcherOpenAiThread, false, null, null);
    }

    @Async
    public CompletableFuture<String> reviewerExtractorResponse(String reviewerMessage) throws Exception {
        return processRequest(reviewerMessage, ReviewerResearcher.INSTRUCTIONS, reviewerResearcher,
                reviewerResearcherThread, false, null, null);
    }

    @Async
    public CompletableFuture<String> summaryResponse(String summaryMessage) throws Exception {
        return processRequest(summaryMessage, SummaryAgent.INSTRUCTIONS, summaryAgent,
                summaryThread, false, null, null);
    }

    @Async
    public CompletableFuture<String> reviewerRankingResponse(String reviewerRankingmessage) throws Exception {
        return processRequest(reviewerRankingmessage, ReviewerRanking.INSTRUCTIONS, reviewerRanking,
                reviewerRankingThread,
                false, null, null);

    }

    @Async
    public CompletableFuture<String> rankingAgentResponse(String rankingAgentmessage) throws Exception {
        return processRequest(rankingAgentmessage, RankingAgent.INSTRUCTIONS, rankingAgent, rankingAgentThread,
                false, null, null);

    }

    // We need the assistant to give instruction to another assistant in order to
    // get a more efficient response
    @Async
    public CompletableFuture<String> correctExtracrorResearcherResponse(String extractorResearcherMessage,
            String reviewer)
            throws Exception {
        // we keep the same thread id
        extractorResearcherMessage = "The reviewer suggest corrections :" + reviewer
                + "in the response of extractor agent which match the cv's in the job position"
                + extractorResearcherMessage
                + "I want to you  to review  again the CV and the csv of job position  based on the suggesting corrections of reviewer agent and return to me the correct a summary of applicant in a CSV format.  ";
        CompletableFuture
                .allOf(extractorResearcherOpenAiThread.addMessage("assistant", extractorResearcherMessage,
                        extractorResearcherThread))
                .join();
        CompletableFuture.allOf(extractorResearcherOpenAiThread.run()).join();
        CompletableFuture<String> response = extractorResearcherOpenAiThread.getRequest();
        CompletableFuture.allOf(response).join();
        return CompletableFuture.completedFuture(response.get());
    }

    // We need the assistant to give instruction to another assistant in order to
    // get a more efficient response
    @Async
    public CompletableFuture<String> correctRankingAgentResponse(String rankingAgentMessage)
            throws Exception {
        // we keep the same thread id
        rankingAgentMessage = "The revier of your result suggrst correction in rsnking process" + rankingAgentMessage;
        CompletableFuture.allOf(reviewerRankingThread.addMessage("assistant", rankingAgentMessage, rankingThread))
                .join();
        CompletableFuture.allOf(reviewerRankingThread.run()).join();
        CompletableFuture<String> response = rankingAgentThread.getRequest();
        CompletableFuture.allOf(response).join();
        return CompletableFuture.completedFuture(response.get());

    }

    // Check if the result of Extractor Reasearcher is the willing
    @Async
    public CompletableFuture<String> checkReviewerResearcherResult(String extractorReasercherResult,
            String reviewerResponse) throws Exception {

        // executing check max 5 times else accept the last answer
        String finalResponse = extractorReasercherResult;
        int count = 0;
        do {
            System.out.println("THE REVIEWER SUGGEST CORRECTIONS");
            CompletableFuture<String> extarctorResearcherCorrections = correctExtracrorResearcherResponse(
                    extractorReasercherResult,
                    reviewerResponse);
            CompletableFuture.allOf(extarctorResearcherCorrections).join();

            extractorReasercherResult = "The response of extractorReasearcher after corrections is: "
                    + extarctorResearcherCorrections.get();
            System.out.println(extractorReasercherResult);
            CompletableFuture<String> reviewerExtractorResponse = reviewerExtractorResponse(extractorReasercherResult);
            CompletableFuture.allOf(reviewerExtractorResponse).join();

            reviewerResponse = reviewerExtractorResponse.get();
            finalResponse = extarctorResearcherCorrections.get();
            count++;
            System.out.println(reviewerResponse.toString());
        } while (count < 1 && !reviewerResponse.contains("---- NO CHANGES REQUIRED, ANALYSIS GOOD ----"));

        return CompletableFuture.completedFuture(finalResponse);
    }

    // Check if the result of Ranking Reasearcher is the willing
    @Async
    public CompletableFuture<String> checkRankingReviewerResult(String rankingAgentResult,
            String reviewerResponse) throws Exception {
        // executing check max 5 times else accept the last answer
        String finalResponse = rankingAgentResult;
        int count = 0;
        do {
            System.out.println("THE REVIEWERRANKING SUGGEST CORRECTIONS");
            CompletableFuture<String> rankingAgentCorrections = correctRankingAgentResponse(reviewerResponse);
            CompletableFuture.allOf(rankingAgentCorrections).join();

            rankingAgentResult = "The response of RankingAgent after corrections is: "
                    + rankingAgentCorrections.get();
            CompletableFuture<String> reviewerRankingResponse = reviewerRankingResponse(rankingAgentResult);
            CompletableFuture.allOf(reviewerRankingResponse).join();

            reviewerResponse = reviewerRankingResponse.get();
            finalResponse = rankingAgentCorrections.get();
            count++;
            System.out.println(reviewerResponse.toString());

        } while (count < 1 && !reviewerResponse.contains("---- NO CHANGES REQUIRED, ANALYSIS GOOD ----"));
        return CompletableFuture.completedFuture(finalResponse);

    }

}