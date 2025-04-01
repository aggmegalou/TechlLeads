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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.database.ranking.RankingRepository;
import com.example.demo.database.ranking.RankingResult;
import com.example.demo.database.ranking.RankingService;

import static org.mockito.Mockito.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RankingServiceTest {

    @Mock
    private RankingRepository rankingRepository; // Mocking the repository

    @InjectMocks
    private RankingService rankingService; // Injecting the mocked repository into the service

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks before each test
    }

    @Test
    void testSaveRankingResult() {
        // Create a new RankingResult object 
        RankingResult rankingResult = new RankingResult();
        rankingResult.setResume("resume example 1");
        rankingResult.setResumeSummary("summary example");
        rankingResult.setSessionId("session123");

        // Mock the ranking repository's save method to return the same object
        when(rankingRepository.save(rankingResult)).thenReturn(rankingResult);

        // Call the ranking service method
        rankingService.saveRankingResult(rankingResult);

        // The repository's save method was called one time only
        verify(rankingRepository, times(1)).save(rankingResult);
    }

    @Test
    void testGetAllRanking() {
        // Mocking the findAll method 
        RankingResult rankingResult1 = new RankingResult("resume1",  "summary1");
        RankingResult rankingResult2 = new RankingResult("resume2","summary2");
        rankingResult1.setSessionId("session123");
        rankingResult2.setSessionId("session456");

        when(rankingRepository.findAll()).thenReturn(List.of(
            rankingResult1,rankingResult2
        
        ));


        // Call the ranking service method
        List<RankingResult> results = rankingService.getAllranking();

        // Verify and assert results
        assertNotNull(results); // Ensure the result list is not null
        assertEquals(2, results.size()); // Verify the number of results

        // Some values of the first result
        assertEquals("resume1", results.get(0).getResume());
        assertEquals("session123", results.get(0).getSessionId());
        assertEquals("summary1", results.get(0).getResumeSummary());

        // Some values of the second result
        assertEquals("resume2", results.get(1).getResume());
        assertEquals("session456", results.get(1).getSessionId());
        assertEquals("summary2", results.get(1).getResumeSummary());

        //  repository's findAll method was called one time
        verify(rankingRepository, times(1)).findAll();
    }
}
