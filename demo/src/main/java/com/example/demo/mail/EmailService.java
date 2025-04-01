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
package com.example.demo.mail;

import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

/**
 * This class represents my class in Java.
 * 
 * @author Aggeliki Despoina Megalou
 * @version 1.0
 */
@Service
public class EmailService {

    private final JavaMailSender emailSender;

    @Autowired
    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    /*
     * public void sendEmail(String to, String subject, String text) throws
     * MessagingException {
     */
    @Async
    public CompletableFuture<String> sendResultEmail(String to, String subject, String text, String user, String sessionId) {

        try {
            // Input validation
            if (to == null || to.isEmpty() || !to.contains("@")) {
                throw new IllegalArgumentException("Invalid email address: " + to);
            }
            if (subject == null || subject.isEmpty()) {
                throw new IllegalArgumentException("Subject cannot be null or empty");
            }
            if (text == null || text.isEmpty()) {
                throw new IllegalArgumentException("Text cannot be null or empty");
            }

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(to);
            helper.setSubject(subject);

            String htmlContent = "<p>Hello " + user + ",</p>" +
                    "<p>Thank you for using HireandGo. Please click the link below to view your confirmation:</p>" +
                    "<p><a href=\"http://localhost:8081/hireandgo/home/ranking/"+sessionId+"\">View Confirmation</a></p>" +
                    "<p>Best regards,<br>HireandGo Team</p>";

            helper.setText(htmlContent, true);

            emailSender.send(message);
            return CompletableFuture.completedFuture("Email sent successfully!");

        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }

    }
}
