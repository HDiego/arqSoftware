/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.dataacces;

import com.reyk.persistence.entities.Booking;
import com.reyk.persistence.entities.Books;
import com.reyk.persistence.entities.Token;
import com.reyk.persistence.entities.Users;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.persistence.PersistenceException;

/**
 *
 * @author MacAA
 */
@Local
public interface PersistenceSBLocal {
    
    void addUser(Users u) throws Exception;
    Users getUser(String username) throws Exception;
    Users getUser(Long id) throws Exception;
    void modifyUser(Users u) throws PersistenceException, Exception;
    void deleteUser(Users user) throws PersistenceException, Exception; 
    
    void addToken(Token t) throws Exception;
    void deleteToken(Token t) throws Exception;
    Token getToken(String t) throws EJBException;
    List<Token> getUserTokens(Users u) throws Exception;
    void deleteUserTokens(Users user) throws Exception;
    
    void addBooking(Booking booking) throws Exception;
    void modifyBooking(Booking booking) throws PersistenceException, Exception;
    void deleteBooking(Booking booking) throws PersistenceException, Exception;
    
    void addBook(Books book) throws Exception;
}
