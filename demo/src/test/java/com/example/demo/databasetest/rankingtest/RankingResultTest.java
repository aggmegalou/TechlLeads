package com.example.demo.databasetest.rankingtest;
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


import com.example.demo.database.ranking.RankingResult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class RankingResultTest {

    @Test
    void testGettersAndSetters() {
        RankingResult rankingResult = new RankingResult();

        
        assertNull(rankingResult.getIdRanking());
        assertNull(rankingResult.getResume());
        assertNull(rankingResult.getResumeSummary());
        assertNull(rankingResult.getSessionId());

        // Test setters and getters
        rankingResult.setIdRanking(1L);
        assertEquals(1L, rankingResult.getIdRanking());

        rankingResult.setResume("resume example");
        assertEquals("resume example", rankingResult.getResume());

        rankingResult.setResumeSummary("summary resume example");
        assertEquals("summary resume example", rankingResult.getResumeSummary());

        rankingResult.setSessionId("session123");
        assertEquals("session123", rankingResult.getSessionId());
    }

    @Test
    void testParameterizedConstructor() {
        // Test constructor with parameters 
        RankingResult rankingResult = new RankingResult("resume example",  "summary resume example");

        rankingResult.setSessionId("session123");
        assertNull(rankingResult.getIdRanking()); // ID should still be null as it's generated
        assertEquals("resume example", rankingResult.getResume());
        assertEquals("summary resume example", rankingResult.getResumeSummary());
        assertEquals("session123", rankingResult.getSessionId());
    }

    @Test
    void testNullValues() {
        RankingResult rankingResult = new RankingResult();
        rankingResult.setResume(null);
        assertNull(rankingResult.getResume());

        rankingResult.setResumeSummary(null);
        assertNull(rankingResult.getResumeSummary());

        rankingResult.setSessionId(null);
        assertNull(rankingResult.getSessionId());
    }

    @Test
    void testEmptyStringResume() {
        RankingResult rankingResult = new RankingResult();
        rankingResult.setResume("");
        assertEquals("", rankingResult.getResume());

        rankingResult.setResumeSummary("");
        assertEquals("", rankingResult.getResumeSummary());

        rankingResult.setSessionId("");
        assertEquals("", rankingResult.getSessionId());
    }

    @Test
    void testToString() {
        RankingResult rankingResult = new RankingResult();
        rankingResult.setIdRanking(1L);
        rankingResult.setResume("resume example");
        rankingResult.setSessionId("session123");
        rankingResult.setResumeSummary("summary resume example");

        String expected = "RankingResult {\n" +
                "    idRanking = 1,\n" +
                "    resumeName = 'resume example',\n" +
                "    sessionId = 'session123',\n" +
                "    summaryOfResume = 'summary resume example'\n" +
                '}';

        assertEquals(expected, rankingResult.toString());
    }
}
