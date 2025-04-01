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

import org.springframework.ui.Model;


import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.database.user.Users;
import com.example.demo.database.user.UsersService;
import com.example.demo.mail.EmailService;
import com.example.demo.openai.service.OpenAiService;

/**
 * @author Maria Spachou,
 * @author Anna Maria Megalou
 * 
 */
@Controller
public class RegistrationFormController {

    @Autowired
    UsersService usersService;

    @Autowired
    OpenAiService openAiService;

    @Autowired
    EmailService emailService;

    // display of registrationform page
    @GetMapping("/hireandgo/home/registrationform")
    public String registrationControl(Model model) {
        model.addAttribute("user", new Users());
        return "registrationform";
    }

    // storing user's data and displaying them
    @PostMapping("/registrationform")
    public String handleRegistration(@RequestParam("file") List<MultipartFile> files,
            @ModelAttribute("user") Users user,
            Model model) throws Exception {

        try {
            System.out.println("Name:" + user.getName());
            System.out.println("Field:" + user.getField());
            System.out.println("Email:" + user.getEmail());
            System.out.println("Hard Skills:" + user.getHardSkills());
            System.out.println("Soft Skills:" + user.getSoftSkills());
            System.out.println("Other Traits:" + user.getOtherTraits());

            for (MultipartFile file : files) {
                System.out.println("File Name: " + file.getOriginalFilename());
                System.out.println("File Size: " + file.getBytes());
            }

            // key=fileName / valou= byte of file
            HashMap<String, byte[]> example = new HashMap<String, byte[]>();

            for (MultipartFile file : files) {
                example.put(file.getOriginalFilename(), file.getBytes());
            }
            usersService.saveUsers(user);
            startRankingProcess(example, user, user.getName(), user.getEmail(), model);

            model.addAttribute("username", user.getName());
        } catch (CancellationException e) {
            System.err.println("Unable to save user");
        }
        return "success";
    }

    @Async
    public CompletableFuture<String> startRankingProcess(HashMap<String, byte[]> files, Users user,
            String name,
            String email, Model model) {

        new Thread(() -> {
            try {
                CompletableFuture<String> result;
                result = openAiService.startRankingProcess(files, user);
                result.join();
                sendResultEmail(name, email, result.get());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();

        return CompletableFuture.completedFuture("ranking");

    }

    @Async
    public CompletableFuture<Void> sendResultEmail(String name, String email, String sessionId) {

        String subject = " Results Ready!";
        String body = "Your processed data and results are ready. Please check provided link for further detailss.";

        CompletableFuture<String> emailResponse = emailService.sendResultEmail(email, subject, body, name, sessionId);

        emailResponse.thenAccept(response -> {
            System.out.println("Email sent successfully: " + response);
        }).exceptionally(ex -> {
            System.err.println("Failed to send email: " + ex.getMessage());
            return null;
        });

        CompletableFuture.allOf(emailResponse).join();
        return CompletableFuture.completedFuture(null);
    }

}