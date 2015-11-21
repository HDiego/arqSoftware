/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.dataTransferObjects;

import java.util.Date;

/**
 *
 * @author MacAA
 */
public class DTOBookings {
    private Long id;    
    private DTOUsers user;
    private DTOBooks books;
    private Date initialDate;
    private Date finalDate;

    public DTOBookings(DTOUsers user, DTOBooks books, Date initialDate, Date finalDate) {
        this.user = user;
        this.books = books;
        this.initialDate = initialDate;
        this.finalDate = finalDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DTOUsers getUser() {
        return user;
    }

    public void setUser(DTOUsers user) {
        this.user = user;
    }

    public DTOBooks getBooks() {
        return books;
    }

    public void setBooks(DTOBooks books) {
        this.books = books;
    }

    public Date getInitialDate() {
        return initialDate;
    }

    public void setInitialDate(Date initialDate) {
        this.initialDate = initialDate;
    }

    public Date getFinalDate() {
        return finalDate;
    }

    public void setFinalDate(Date finalDate) {
        this.finalDate = finalDate;
    }
    
    
    
}
