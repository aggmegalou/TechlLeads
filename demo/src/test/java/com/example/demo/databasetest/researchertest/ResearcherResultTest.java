package com.example.demo.databasetest.researchertest;
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

import com.example.demo.database.researcher.ResearcherResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ResearcherResultTest {

    @Test
    void testGettersAndSetters() {
        ResearcherResult researcherResult = new ResearcherResult();

    
        assertNull(researcherResult.getIdResearcher());
        assertNull(researcherResult.getResume());
        assertNull(researcherResult.getFileName());
        assertNull(researcherResult.getSessionId());

        // Test setters and getters
        researcherResult.setIdResearcher(1L);
        assertEquals(1L, researcherResult.getIdResearcher());

        researcherResult.setResume("resume example");
        assertEquals("resume example", researcherResult.getResume());

        researcherResult.setFileName("cv.pdf");
        assertEquals("cv.pdf", researcherResult.getFileName());

        researcherResult.setSessionId("session123");
        assertEquals("session123", researcherResult.getSessionId());
    }

    @Test
    void testParameterizedConstructor() {
        // Test constructor with parameters 
        ResearcherResult researcherResult = new ResearcherResult("cv.pdf", "resume example");

        researcherResult.setSessionId("session123");
        assertNull(researcherResult.getIdResearcher()); // ID should still be null as it's generated
        assertEquals("resume example", researcherResult.getResume());
        assertEquals("cv.pdf", researcherResult.getFileName());
        assertEquals("session123", researcherResult.getSessionId());
    }

    @Test
    void testNullValues() {
        ResearcherResult researcherResult = new ResearcherResult();
        
        researcherResult.setResume(null);
        assertNull(researcherResult.getResume());
        
        researcherResult.setFileName(null);
        assertNull(researcherResult.getFileName());

        researcherResult.setSessionId(null);
        assertNull(researcherResult.getSessionId());
    }

    @Test
    void testEmptyStringValues() {
        ResearcherResult researcherResult = new ResearcherResult();
        
        researcherResult.setResume("");
        assertEquals("", researcherResult.getResume());
        
        researcherResult.setFileName("");
        assertEquals("", researcherResult.getFileName());

        researcherResult.setSessionId("");
        assertEquals("", researcherResult.getSessionId());
    }

    @Test
    void testToString() {
        ResearcherResult researcherResult = new ResearcherResult();
        
        researcherResult.setIdResearcher(1L);
        researcherResult.setResume("resume example");
        researcherResult.setFileName("cv.pdf");
        researcherResult.setSessionId("session123");

        String expected = "ResearcherResult {\n" +
                "    idResearcher = 1,\n" +
                "    fileName = 'cv.pdf'\n" +
                "    resumeName = 'resume example',\n" +
                "    sessionId = 'session123',\n" +
                '}';

        assertEquals(expected, researcherResult.toString());
    }
}
