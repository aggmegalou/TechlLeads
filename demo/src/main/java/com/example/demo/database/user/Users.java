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

/**
 * This class represents my class in Java.
 * 
 * <p>It is designed to demonstrate how to declare the author of a class
 * using a JavaDoc comment.</p>
 * 
 * @author Konstantia Stergiou
 * @version 1.0
 */

package com.example.demo.database.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class Users {
   
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PrimaryKeyJoinColumn
    private Long idUsers;

    @Column(name = "name", nullable = false, length = 100)
    private String name;
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;
    @Column(name = "field", length = 100)
    private String field;
    @Column(name = "hard_skills", columnDefinition = "TEXT")
    private String hardSkills;
    @Column(name = "soft_skills", columnDefinition = "TEXT")
    private String softSkills;
    @Column(name = "other_traits", columnDefinition = "TEXT")
    private String otherTraits;

    // CONSTRUCTORS

    public Users() {

    }

  public Users(String name, String email, String field, String hardSkills,String softSkills, String otherTraits) {
    this.name = name;
     this.email = email;
     this.field = field;
     this.hardSkills = hardSkills;
     this.softSkills = softSkills;
     this.otherTraits = otherTraits;
  }

    // Getters and Setters

    public Long getIdUsers() {

        return idUsers;
    }

    public void setIdUsers(Long idUsers) {
        this.idUsers = idUsers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getHardSkills() {
        return hardSkills;

    }

    public void setHardSkills(String hardSkills) {
        this.hardSkills = hardSkills;

    }

    public String getSoftSkills() {
        return softSkills;

    }

    public void setSoftSkills(String softSkills) {
        this.softSkills = softSkills;

    }

    public String getOtherTraits() {
        return otherTraits;

    }

    public void setOtherTraits(String otherTraits) {
        this.otherTraits = otherTraits;

    }


    @Override
public String toString() {
    return "Users {\n" +
            "    idUsers = " + idUsers + ",\n" +
            "    name = '" + name + "',\n" +
            "    email = '" + email + "',\n" +
            "    field = '" + field + "',\n" +
            "    hardSkills = '" + hardSkills + "',\n" +
            "    softSkills = '" + softSkills + "',\n" +
            "    otherTraits = '" + otherTraits + "'\n" +
            "}";
}



}
