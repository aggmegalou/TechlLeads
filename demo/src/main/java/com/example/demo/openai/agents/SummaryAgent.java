package com.example.demo.openai.agents;
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

import org.springframework.stereotype.Service;

/**
 * This class represents my class in Java.
 * @author Anna Maria Megalou
 * @version 1.0
 */

 @Service
public class SummaryAgent extends OpenAiAssistant {
    
    public static final String INSTRUCTIONS = "You are responsible for a cv ranking procedure where other agents are part of as well. Your role is to get a csv that match a csv resume with a job position in csv format and RETURN A TEXT WITH A SUMMARY (20-30 WORDS) OF HOW ABLE IS THE APPLICANT FOR THAT JOB POSITION. ALSO TO EMPHASIZE IN ABILITIES OF APPLICANTS AND DISABILITIES FOR THE SPECIFIC JOB POSITION .I dont want the summary in csv format but want to return a small text(paragraph)";
    public static final String MODEL = "gpt-4o-mini";
    public static final String NAME = "Summary";

    {
        instructions = INSTRUCTIONS;
        model = MODEL;
        name = NAME;
    }
}
