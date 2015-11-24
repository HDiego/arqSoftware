/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.entities;

import java.util.Date;
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
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author MacAA
 */

@NamedQueries({
    @NamedQuery(name = "getBookingsByUser",
            query = "select b from Booking b where b.user = :idUsers"),
    @NamedQuery(name = "getOneBooking",
            query = "select b from Booking b where b.user = :idUser and b.initialDate = :dateInit")})

@Entity
@Table(name = "Booking")
public class Booking {
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_Id")
    private Long id;    
    
    @ManyToOne()
    @JoinColumn(name = "user_Id") 
    private Users user;
    
    @ManyToOne()
    @JoinColumn(name = "book_Id")
    private Books books;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date initialDate;
    
    @NotNull
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date finalDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Books getBooks() {
        return books;
    }

    public void setBooks(Books books) {
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
    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Booking)) {
            return false;
        }
        Booking b = (Booking) obj;
        return b.id == this.id;
    }
    
}
