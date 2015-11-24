/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.entities;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MacAA
 */

@NamedQueries({
    @NamedQuery(name = "getUser",
            query = "select u from Users u where u.username = :user"),
    @NamedQuery(name = "getAllUsers",
            query = "select u from Users u")})
@Entity
@Table(name = "Users")
public class Users {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_Id")
    private Long id;
    
    @NotNull
    private String name;
    @NotNull
    private String surname;
    
    @NotNull
    @Column(unique = true)
    private String username;
    
    @NotNull
    private String password;
    
    @NotNull
    private String email;
    
    private boolean suscribed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSuscribed() {
        return suscribed;
    }

    public void setSuscribed(boolean suscribed) {
        this.suscribed = suscribed;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.id);
        return hash;
    }

   @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Users)) {
            return false;
        }
        Users u = (Users) obj;
        return u.id == this.id;
    }
     
    
}
