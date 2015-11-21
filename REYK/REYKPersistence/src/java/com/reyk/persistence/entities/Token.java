/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author MacAA
 */
@NamedQueries({
    @NamedQuery(name="getToken",
    query="select t from Token t where t.token = :token"
    ),
    @NamedQuery(name="getUserTokens",
    query="select t from Token t where t.user = :idUser"
    )})
@Entity
@Table(name = "Tokens")
public class Token implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
       
    @Column(unique = true)
    private String token;
    
    @ManyToOne()
    @JoinColumn(name = "user_Id") 
    private Users user;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    @Override
    public String toString() {
        return "com.reyk.persistence.entities.Token[ id=" + id + " ]";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Token)) {
            return false;
        }
        Token t = (Token) obj;
        return t.id == this.id;
    }
    
}
