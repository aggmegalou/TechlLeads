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

import org.springframework.stereotype.Service;

/**
 * This class represents my class in Java.
 * 
 * @author Aggeliki Despoina Megalou
 * @version 1.0
 */
@Service
public class ReviewerRanking extends OpenAiAssistant{
    
    public static final String INSTRUCTIONS = "You are part of a system that reviews CVs from candidates on different domains. YOU ARE THE REVIEWER IN THAT HIRING COMPANY THAT TAKES THE FINAL DECISION. YOUR ANSWER WILL IMPACT THE COMPANY."+
                                        "Based on that I want you to review  the RankingAgent’s result and estimate if the ranking of applicants is correct./n"+
                                        "Your answer should consist of either 2 responses:"+
                                        "1. ---- REQUIRES CHANGES ----/n"+
                                        "2. ---- NO CHANGES REQUIRED, ANALYSIS GOOD ----/n"+
                                        "If changes are required you will need to add feedback for the agents to correct their response Otherwise you are free to respond only with the /'---- NO CHANGES REQUIRED, ANALYSIS GOOD ----/'";

    public static final String MODEL = "gpt-4o-mini";
    public static final String NAME = "ReviewerRanking";

    {
        instructions = INSTRUCTIONS;
        model = MODEL;
        name = NAME;
    }

    
}
    