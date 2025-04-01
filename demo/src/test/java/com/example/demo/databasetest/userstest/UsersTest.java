package com.example.demo.databasetest.userstest;

import org.junit.jupiter.api.Test;

import com.example.demo.database.user.Users;

import static org.junit.jupiter.api.Assertions.*;

public class UsersTest {

    @Test
    void testParameterizedConstructor() {
        Users user = new Users("Finovatech Solutions", "oly.meg@finovatech.com", "Finance", 
                               "Financial analysis and modeling", "Analytical thinking, Attention to detail, Risk management mindset", 
                               "Integrity, Curiosity, Resilience, Discipline");

        assertNull(user.getIdUsers()); // ID should remain null as it is generated
        assertEquals("Finovatech Solutions", user.getName());
        assertEquals("oly.meg@finovatech.com", user.getEmail());
        assertEquals("Finance", user.getField());
        assertEquals("Financial analysis and modeling", user.getHardSkills());
        assertEquals("Analytical thinking, Attention to detail, Risk management mindset", user.getSoftSkills());
        assertEquals("Integrity, Curiosity, Resilience, Discipline", user.getOtherTraits());
    }

    @Test
    void testGettersAndSetters() {
        Users user = new Users();

        user.setIdUsers(1L);
        assertEquals(1L, user.getIdUsers());

        user.setName("Finovatech Solutions");
        assertEquals("Finovatech Solutions", user.getName());

        user.setEmail("oly.meg@finovatech.com");
        assertEquals("oly.meg@finovatech.com", user.getEmail());

        user.setField("Finance");
        assertEquals("Finance", user.getField());

        user.setHardSkills("Financial analysis and modeling");
        assertEquals("Financial analysis and modeling", user.getHardSkills());

        user.setSoftSkills("Analytical thinking, Attention to detail, Risk management mindset");
        assertEquals("Analytical thinking, Attention to detail, Risk management mindset", user.getSoftSkills());

        user.setOtherTraits("Integrity, Curiosity, Resilience, Discipline");
        assertEquals("Integrity, Curiosity, Resilience, Discipline", user.getOtherTraits());
    }

    @Test
    void testToString() {
        Users user = new Users();
        user.setIdUsers(1L);
        user.setName("Finovatech Solutions");
        user.setEmail("oly.meg@finovatech.com");
        user.setField("Finance");
        user.setHardSkills("Financial analysis and modeling");
        user.setSoftSkills("Analytical thinking, Attention to detail, Risk management mindset");
        user.setOtherTraits("Integrity, Curiosity, Resilience, Discipline");

        String expected = "Users {\n" +
    "    idUsers = 1,\n" +
    "    name = 'Finovatech Solutions',\n" +
    "    email = 'oly.meg@finovatech.com',\n" +
    "    field = 'Finance',\n" +
    "    hardSkills = 'Financial analysis and modeling',\n" +
    "    softSkills = 'Analytical thinking, Attention to detail, Risk management mindset',\n" +
    "    otherTraits = 'Integrity, Curiosity, Resilience, Discipline'\n" +
    "}";
        assertEquals(expected, user.toString());
    }

    @Test
    void testNullValues() {
        Users user = new Users();
        
        user.setName(null);
        assertNull(user.getName());

        user.setEmail(null);
        assertNull(user.getEmail());

        user.setField(null);
        assertNull(user.getField());

        user.setHardSkills(null);
        assertNull(user.getHardSkills());

        user.setSoftSkills(null);
        assertNull(user.getSoftSkills());

        user.setOtherTraits(null);
        assertNull(user.getOtherTraits());
    }

    @Test
    void testEmptyStringValues() {
        Users user = new Users();

        user.setName("");
        assertEquals("", user.getName());

        user.setEmail("");
        assertEquals("", user.getEmail());

        user.setField("");
        assertEquals("", user.getField());

        user.setHardSkills("");
        assertEquals("", user.getHardSkills());

        user.setSoftSkills("");
        assertEquals("", user.getSoftSkills());

        user.setOtherTraits("");
        assertEquals("", user.getOtherTraits());
    }
}
