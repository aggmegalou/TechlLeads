package com.example.demo.databasetest.userstest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.demo.database.user.Users;
import com.example.demo.database.user.UsersService;
import com.example.demo.database.user.UserRepository;

import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

class UsersServiceTest {

    @Mock
    private UserRepository userRepository; // Mocking the repository

    @InjectMocks
    private UsersService usersService; // Service i want to test

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
    }

    @Test
    void testSaveUsers() {
        // Create a new user with some values
        Users user = new Users();
        user.setName("Finovatech Solutions");
        user.setEmail("oly.meg@finovatech.com");
        user.setField("Finance");
        user.setHardSkills("Financial analysis and modeling");
        user.setSoftSkills("Analytical thinking, Attention to detail, Risk management mindset");
        user.setOtherTraits("Integrity, Curiosity, Resilience, Discipline");

        // Mock the user's repository save method
        when(userRepository.save(user)).thenReturn(user);

        // Call the service method asynchronously cause i used the async annotation in the usersservice class
        CompletableFuture<Void> result = usersService.saveUsers(user);

        // Wait for the result to complete 
        result.join();  // wait until the async operation completes

        // Verify the repository's save method was called one time
        verify(userRepository, times(1)).save(user);
    }
}
