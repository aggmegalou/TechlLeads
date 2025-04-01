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
package com.example.demo.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.example.demo.database.user.Users;
import com.example.demo.database.user.UsersService;
import com.example.demo.mail.EmailService;
import com.example.demo.openai.service.OpenAiService;

/**
 * @author Maria Spachou
 */
@WebMvcTest(RegistrationFormController.class)
class RegistrationFormControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private UsersService usersService;

        @MockBean
        private OpenAiService openAiService;

        @MockBean
        private EmailService emailService;

        @Test
        void testRegistration() throws Exception {
                // Εδώ εκτελούμε το αίτημα και ελέγχουμε την κατάσταση της απόκρισης και το view
                mockMvc.perform(get("/hireandgo/home/registrationform"))
                       .andExpect(status().isOk())  // Ελέγχουμε ότι η κατάσταση της απόκρισης είναι 200 OK
                       .andExpect(view().name("registrationform"));  // Ελέγχουμε ότι το όνομα του view είναι "home"
            }

        @Test
        void testHandleRegistration() throws Exception {
                // Mock δεδομένα χρήστη
                Users user = new Users();
                user.setName("Maria");
                user.setField("Computer Science");
                user.setEmail("maria@example.com");

                // Mock αρχείο
                MockMultipartFile mockFile = new MockMultipartFile("file", "test.txt", "text/plain",
                                "Test content".getBytes());

                // Mock υπηρεσίες
                doAnswer(
                                (InvocationOnMock invocation) -> {
                                        return null;
                                }).when(usersService).saveUsers(any(Users.class));
                doAnswer(
                                (InvocationOnMock invocation) -> {
                                        return null;
                                }).when(openAiService).startRankingProcess(any(), any(Users.class));

                mockMvc.perform(multipart("/registrationform")
                                .file(mockFile)
                                .param("name", "Maria")
                                .param("field", "Computer Science")
                                .param("email", "maria@example.com")
                                .flashAttr("user", user))
                                .andExpect(status().isOk())
                                .andExpect(view().name("success"))
                                .andExpect(model().attribute("username", "Maria"));

                // Επιβεβαίωση ότι οι υπηρεσίες καλούνται
                verify(usersService, times(1)).saveUsers(any(Users.class));
                verify(openAiService, times(1)).startRankingProcess(any(), any(Users.class));
        }

        @Test
        void testSendResultEmail() {
            // Mock δεδομένα
            String name = "Maria";
            String email = "maria@example.com";
            String sessionId = "12345";
            String subject = " Results Ready!";
            String body = "Your processed data and results are ready. Please check provided link for further detailss.";
        
            // Mock απάντηση από το emailService
            CompletableFuture<String> mockEmailResponse = CompletableFuture.completedFuture("Email sent successfully");
            
            // Ρύθμιση του mock για το emailService.sendResultEmail
            when(emailService.sendResultEmail(email, subject, body, name, sessionId)).thenReturn(mockEmailResponse);
        
            // Δημιουργία instance του controller
            RegistrationFormController controller = new RegistrationFormController();
            controller.emailService = emailService; // Χειροκίνητη ρύθμιση του emailService mock
        
            // Κλήση της μεθόδου sendResultEmail
            CompletableFuture<Void> result = controller.sendResultEmail(name, email, sessionId);
        
            // Επαλήθευση ότι το emailService καλείται σωστά
            verify(emailService, times(1)).sendResultEmail(email, subject, body, name, sessionId);
        
            // Επαλήθευση ότι το CompletableFuture ολοκληρώνεται χωρίς σφάλματα
            result.join();
        }
}