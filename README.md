#Techleads

# Hire&Go

## Project Description

**Hire&Go** is a software application designed to optimize the candidate selection process in businesses by leveraging AI assistants. It efficiently ranks resumes based on their suitability for a specific job position. 

The process of using the application is as follows:
1. **Complete the form**: The user (company representative) provides their details and specifies the job position.
2. **Attach resumes**: Users upload resumes to the form (preferred formats: `.doc` and `.pdf`).
3. **Resume ranking**: The system uses OpenAI assistants to rank resumes based on their relevance.
4. **Results delivery**: An email is sent to the user containing a link to a webpage where the ranked resumes can be reviewed.

---

## Workflow Illustration

The following images will illustrate the workflow or the prodject


 Welcome to our Home Page! Here, you can learn about our Services, discover key details About Us, and find our Contact Information.
 When you click on "Get Started," you’ll be redirected to our Registration Form.

![Our Home page](demo/src/main/resources/static/readme-images/Home1.png)

![Our Services](demo/src/main/resources/static/readme-images/Home2.png)

![About us](demo/src/main/resources/static/readme-images/Home3.png)

![Contact us information](demo/src/main/resources/static/readme-images/Home4.png)

 Fill in the required fields to complete your registration.

![Steps to fill out the Registration Form](demo/src/main/resources/static/readme-images/Reg1.png)

 Drag all the files at once.

![Dragging all the Files at once](demo/src/main/resources/static/readme-images/Reg3.png)

![Steps to Fill Out the Registration Form](demo/src/main/resources/static/readme-images/Reg2.png)

 Your Registration is Successful! Shortly, you will receive an email from Us with a link to the Ranked Candidates.

![Registration Successful](demo/src/main/resources/static/readme-images/RegSuccess.png)

 Follow the link to view the Ranked Candidates!

![Email with Link Sent to your Inbox](demo/src/main/resources/static/readme-images/Email.png)

![The Ranked Candidates](demo/src/main/resources/static/readme-images/RankedCandidates.png)


---

## Compilation Instructions

### Prerequisites
- **Java Development Kit (JDK)** version 17 or higher  
- **Apache Maven**
- **PostgreSQL**

### Compilation Steps
1. Check Maven installation:  
   ```bash
   mvn -v
2. Clean the prodject:
   ```bash
   mvn clean
3. Built the prodject:
   ```bash
   mvn clean install
4. Compile the prodject:
   ```bash
   mvn clean compile
5. Package the application:
   ```bash
   mvn package
6. Run tests:
   ```bash
   mvn test 
7. Execute the application as a standalone JAR file:
   ```bash
   java -jar target/<file-name>.jar

---

## Execution Instructions 

1. To run the program:
   ```bash
   mvn spring-boot:run

---

## User Guide

1. Clone the Repository: Clone the TechLeads repository from GitHub using the command:
   ```bash
   git clone https://github.com/Anna8230094/TechLeads.git
2. Set Up env.properties: Create an env.properties file based on the env.properties.example file. Populate the required fields in the resources directory, including:
   - **Database connection details**
   - **OpenAI key**
3. Install PostgreSQL: Ensure that PostgreSQL is installed on your system before running the application.
4. Upload Resumes: When running the program, all resumes must be uploaded simultaneously by dragging them into the form on the `RegistrationForm.html` page.
5. Tip: During execution, the first page users will see is the **Home Page**. Access it in a browser using:
   ```text   
   http://localhost:8081/hireandgo/home/

---

## UML Diagram: Code Design

![UML Diagram of our Code](demo/src/main/resources/static/readme-images/techleadsUML.png)

---

## Project Structure
 ```Folder PATH listing for volume Windows
Volume serial number is F2EB-5143
C:.
ο   LICENSE
ο   NOTICE.md
ο   README.md
ο   structure.txt
ο   
+---.vscode
ο       settings.json
ο       
L---demo
    ο   .gitattributes
    ο   .gitignore
    ο   mvnw
    ο   mvnw.cmd
    ο   pom.xml
    ο   
    +---.mvn
    ο   L---wrapper
    ο           maven-wrapper.properties
    ο           
    +---src
    ο   +---main
    ο   ο   +---java
    ο   ο   ο   L---com
    ο   ο   ο       L---example
    ο   ο   ο           L---demo
    ο   ο   ο               ο   DemoApplication.java
    ο   ο   ο               ο   
    ο   ο   ο               +---controller
    ο   ο   ο               ο       HomeController.java
    ο   ο   ο               ο       RankingController.java
    ο   ο   ο               ο       RegistrationFormController.java
    ο   ο   ο               ο       SuccessController.java
    ο   ο   ο               ο       
    ο   ο   ο               +---database
    ο   ο   ο               ο   +---ranking
    ο   ο   ο               ο   ο       EqualsAndHashCode.java
    ο   ο   ο               ο   ο       RankingRepository.java
    ο   ο   ο               ο   ο       RankingResult.java
    ο   ο   ο               ο   ο       RankingService.java
    ο   ο   ο               ο   ο       
    ο   ο   ο               ο   +---researcher
    ο   ο   ο               ο   ο       ResearcherRepository.java
    ο   ο   ο               ο   ο       ResearcherResult.java
    ο   ο   ο               ο   ο       ResearcherService.java
    ο   ο   ο               ο   ο       
    ο   ο   ο               ο   L---user
    ο   ο   ο               ο           UserRepository.java
    ο   ο   ο               ο           Users.java
    ο   ο   ο               ο           UsersService.java
    ο   ο   ο               ο           
    ο   ο   ο               +---mail
    ο   ο   ο               ο       EmailSender.java
    ο   ο   ο               ο       EmailService.java
    ο   ο   ο               ο       
    ο   ο   ο               L---openai
    ο   ο   ο                   +---agents
    ο   ο   ο                   ο       Extractor.java
    ο   ο   ο                   ο       ExtractorResearcher.java
    ο   ο   ο                   ο       OpenAiAssistant.java
    ο   ο   ο                   ο       RankingAgent.java
    ο   ο   ο                   ο       Register.java
    ο   ο   ο                   ο       ReviewerRanking.java
    ο   ο   ο                   ο       ReviewerResearcher.java
    ο   ο   ο                   ο       SummaryAgent.java
    ο   ο   ο                   ο       
    ο   ο   ο                   +---service
    ο   ο   ο                   ο       OpenAiService.java
    ο   ο   ο                   ο       
    ο   ο   ο                   L---threads
    ο   ο   ο                           ExtractorThread.java
    ο   ο   ο                           OpenAiThread.java
    ο   ο   ο                           
    ο   ο   L---resources
    ο   ο       ο   application.properties
    ο   ο       ο   env.properties.example
    ο   ο       ο   
    ο   ο       +---static
    ο   ο       ο   +---css
    ο   ο       ο   ο       home.css
    ο   ο       ο   ο       registration.css
    ο   ο       ο   ο       success.css
    ο   ο       ο   ο       
    ο   ο       ο   +---images
    ο   ο       ο   ο       assept-document.png
    ο   ο       ο   ο       candidates.jpg
    ο   ο       ο   ο       cv.jpg
    ο   ο       ο   ο       handshake (1).png
    ο   ο       ο   ο       join.jpg
    ο   ο       ο   ο       list.jpg
    ο   ο       ο   ο       people.png
    ο   ο       ο   ο       team.png
    ο   ο       ο   ο       user.png
    ο   ο       ο   ο       
    ο   ο       ο   +---js
    ο   ο       ο   ο       script.js
    ο   ο       ο   ο       
    ο   ο       ο   L---readme-images
    ο   ο       ο           Email.png
    ο   ο       ο           Home1.png
    ο   ο       ο           Home2.png
    ο   ο       ο           Home3.png
    ο   ο       ο           Home4.png
    ο   ο       ο           RankedCandidates.png
    ο   ο       ο           Reg1.png
    ο   ο       ο           Reg2.png
    ο   ο       ο           Reg3.png
    ο   ο       ο           RegSuccess.png
    ο   ο       ο           techleadsUML.png
    ο   ο       ο           
    ο   ο       L---templates
    ο   ο               home.html
    ο   ο               ranking.html
    ο   ο               registrationform.html
    ο   ο               success.html
    ο   ο               
    ο   L---test
    ο       L---java
    ο           L---com
    ο               L---example
    ο                   L---demo
    ο                       +---controller
    ο                       ο       HomeControllerTest.java
    ο                       ο       RankingControllerTest.java
    ο                       ο       RegistrationFormControllerTest.java
    ο                       ο       SuccessControllerTest.java
    ο                       ο       
    ο                       +---databasetest
    ο                       ο   +---rankingtest
    ο                       ο   ο       RankingResultTest.java
    ο                       ο   ο       RankingServiceTest.java
    ο                       ο   ο       
    ο                       ο   +---researchertest
    ο                       ο   ο       ResearcherResultTest.java
    ο                       ο   ο       ResearcherServiceTest.java
    ο                       ο   ο       
    ο                       ο   L---userstest
    ο                       ο           UsersServiceTest.java
    ο                       ο           UsersTest.java
    ο                       ο           
    ο                       +---email
    ο                       ο       EmailSenderTest.java
    ο                       ο       EmailServiceTest.java
    ο                       ο       
    ο                       L---openai
    ο                               ExtractorResearcherTest.java
    ο                               ExtractorTest.java
    ο                               OpenAiAssistantTest.java
    ο                               OpenAiExtractorThread.java
    ο                               OpenAiThreadTest.java
    ο                               RankingTest.java
    ο                               RegisterTest.java
    ο                               ReviewerRankingTest.java
    ο                               ReviewerResearcherTest.java
    ο                               SummaryTest.java
    ο                               
    L---target
        +---classes
        ο   ο   application.properties
        ο   ο   env.properties.example
        ο   ο   
        ο   +---com
        ο   ο   L---example
        ο   ο       L---demo
        ο   ο           ο   DemoApplication.class
        ο   ο           ο   
        ο   ο           +---controller
        ο   ο           ο       HomeController.class
        ο   ο           ο       RankingController.class
        ο   ο           ο       RegistrationFormController.class
        ο   ο           ο       SuccessController.class
        ο   ο           ο       
        ο   ο           +---database
        ο   ο           ο   +---ranking
        ο   ο           ο   ο       EqualsAndHashCode.class
        ο   ο           ο   ο       RankingRepository.class
        ο   ο           ο   ο       RankingResult.class
        ο   ο           ο   ο       RankingService.class
        ο   ο           ο   ο       
        ο   ο           ο   +---researcher
        ο   ο           ο   ο       ResearcherRepository.class
        ο   ο           ο   ο       ResearcherResult.class
        ο   ο           ο   ο       ResearcherService.class
        ο   ο           ο   ο       
        ο   ο           ο   L---user
        ο   ο           ο           UserRepository.class
        ο   ο           ο           Users.class
        ο   ο           ο           UsersService.class
        ο   ο           ο           
        ο   ο           +---mail
        ο   ο           ο       EmailSender.class
        ο   ο           ο       EmailService.class
        ο   ο           ο       
        ο   ο           L---openai
        ο   ο               +---agents
        ο   ο               ο       Extractor.class
        ο   ο               ο       ExtractorResearcher.class
        ο   ο               ο       OpenAiAssistant.class
        ο   ο               ο       RankingAgent.class
        ο   ο               ο       Register.class
        ο   ο               ο       ReviewerRanking.class
        ο   ο               ο       ReviewerResearcher.class
        ο   ο               ο       SummaryAgent.class
        ο   ο               ο       
        ο   ο               +---service
        ο   ο               ο       OpenAiService.class
        ο   ο               ο       
        ο   ο               L---threads
        ο   ο                       ExtractorThread.class
        ο   ο                       OpenAiThread.class
        ο   ο                       
        ο   +---static
        ο   ο   +---css
        ο   ο   ο       home.css
        ο   ο   ο       registration.css
        ο   ο   ο       success.css
        ο   ο   ο       
        ο   ο   +---images
        ο   ο   ο       assept-document.png
        ο   ο   ο       candidates.jpg
        ο   ο   ο       cv.jpg
        ο   ο   ο       handshake (1).png
        ο   ο   ο       join.jpg
        ο   ο   ο       list.jpg
        ο   ο   ο       people.png
        ο   ο   ο       team.png
        ο   ο   ο       user.png
        ο   ο   ο       
        ο   ο   +---js
        ο   ο   ο       script.js
        ο   ο   ο       
        ο   ο   L---readme-images
        ο   ο           Email.png
        ο   ο           Home1.png
        ο   ο           Home2.png
        ο   ο           Home3.png
        ο   ο           Home4.png
        ο   ο           RankedCandidates.png
        ο   ο           Reg1.png
        ο   ο           Reg2.png
        ο   ο           Reg3.png
        ο   ο           RegSuccess.png
        ο   ο           techleadsUML.png
        ο   ο           
        ο   L---templates
        ο           home.html
        ο           ranking.html
        ο           registrationform.html
        ο           success.html
        ο           
        L---test-classes
            L---com
                L---example
                    L---demo
                        +---controller
                        ο       HomeControllerTest.class
                        ο       RankingControllerTest.class
                        ο       RegistrationFormControllerTest.class
                        ο       SuccessControllerTest.class
                        ο       
                        +---databasetest
                        ο   +---rankingtest
                        ο   ο       RankingResultTest.class
                        ο   ο       RankingServiceTest.class
                        ο   ο       
                        ο   +---researchertest
                        ο   ο       ResearcherResultTest.class
                        ο   ο       ResearcherServiceTest.class
                        ο   ο       
                        ο   L---userstest
                        ο           UsersServiceTest.class
                        ο           UsersTest.class
                        ο           
                        +---email
                        ο       EmailSenderTest.class
                        ο       EmailServiceTest.class
                        ο       
                        L---openai
                                ExtractorResearcherTest.class
                                ExtractorTest.class
                                OpenAiAssistantTest.class
                                OpenAiExtractorThread.class
                                OpenAiThreadTest.class
                                RankingTest.class
                                RegisterTest.class
                                ReviewerRankingTest.class
                                ReviewerResearcherTest.class
                                SummaryTest.class
                                
```

---

## Licenses

This project integrates OpenAI services. Use of OpenAI APIs and services is governed by OpenAI's Terms of Service ([Terms of Service](https://openai.com/terms)) and API Terms ([API Terms](https://openai.com/api-terms)). Users of this project must comply with these terms.

---

## Dependencies and their Licenses

This project uses the following dependencies:

- **Apache License 2.0**: Licensed under the [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).
- **Eclipse Public License 2.0**: Licensed under the [Eclipse Public License 2.0](https://www.eclipse.org/legal/epl-2.0/).
- **MIT License**: Licensed under the [MIT License](https://opensource.org/licenses/MIT).

For more information on each dependency and its licensing, please refer to their respective license links.