/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.entities;

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
 * @author diegorocca
 */

@NamedQueries({
    @NamedQuery(name = "getMessages",
            query = "select m from Messages m where m.user = :idUser and m.seen = false"),
    @NamedQuery(name = "getAllMessages",
            query = "select m from Messages m")
})

@Entity
@Table(name = "Messages")
public class Messages {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "message_Id")
    private Long id;
    
    private String message;
    
    private boolean seen;
    
    @ManyToOne()
    @JoinColumn(name = "user_Id")
    private Users user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
    
    
}
