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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.database.researcher.ResearcherRepository;
import com.example.demo.database.researcher.ResearcherResult;
import com.example.demo.database.researcher.ResearcherService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;


class ResearcherServiceTest {

    @Mock
    private ResearcherRepository researcherRepository; 

    @InjectMocks
    private ResearcherService researcherService; 
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); 
    }

    @Test
    void testSaveResearcherResult() {
        // Create a new ResearcherResult object
        ResearcherResult researcherResult = new ResearcherResult("cv.pdf", "resume example 1");

        researcherResult.setSessionId("session123");
        // Mock the repository's save method to return the same object
        when(researcherRepository.save(researcherResult)).thenReturn(researcherResult);

        // Call the researcher service method
        researcherService.saveResearcherResult(researcherResult);

        // Verify that the repository's save method was called one time
        verify(researcherRepository, times(1)).save(researcherResult);

        // Assert the variables of ResearcherResult 
        assertNotNull(researcherResult);
        assertEquals("resume example 1", researcherResult.getResume());
        assertEquals("cv.pdf", researcherResult.getFileName());
        assertEquals("session123", researcherResult.getSessionId());
    }

    @Test
    void testGetAllResearcher() {
        // Create mock data
        ResearcherResult researcherResult1 = new ResearcherResult("cv1.pdf", "resume1");
        ResearcherResult researcherResult2 = new ResearcherResult("cv2.pdf", "resume2");

        List<ResearcherResult> mockResults = List.of(researcherResult1, researcherResult2);

        // Mock the repository method findBySessionId
        when(researcherRepository.findBySessionId("session123")).thenReturn(mockResults);

        // Call the ranking service method
        List<ResearcherResult> results = researcherService.getAllresearcher("session123");

        // Verify the repository call and assert results
        verify(researcherRepository, times(1)).findBySessionId("session123");
        assertNotNull(results);
        assertEquals(2, results.size());
        assertEquals("resume1", results.get(0).getResume());
        assertEquals("resume2", results.get(1).getResume());
    }

   
}
