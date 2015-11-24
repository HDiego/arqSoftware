/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.dataacces;

import com.reyk.persistence.entities.Booking;
import com.reyk.persistence.entities.Books;
import com.reyk.persistence.entities.Messages;
import com.reyk.persistence.entities.Token;
import com.reyk.persistence.entities.Users;
import java.util.List;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.TemporalType;

/**
 *
 * @author MacAA
 */
@Stateless
public class PersistenceSB implements PersistenceSBLocal {

    @PersistenceContext
    private EntityManager em;

    public PersistenceSB() {

    }

    //<editor-fold defaultstate="collapsed" desc="Users">
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addUser(Users u) throws Exception {
        try {

            em.persist(u);
        } catch (Exception e) {
            throw new Exception("User already exists", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users getUser(String username) throws Exception {
        try {
            return (Users) em.createNamedQuery("getUser").setParameter("user", username).getSingleResult();

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users getUser(Long id) throws Exception {
        return em.find(Users.class, id);
    }

    @Override
    public List<Users> getUsers() throws Exception {
        try {
            List<Users> listUser = (List<Users>) em.createNamedQuery("getAllUsers").getResultList();
            return listUser;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteUser(Users user) throws PersistenceException, Exception {
        if (user.getId() != null) {
            user = em.merge(user);
            this.deleteUserTokens(user);
            em.remove(user);
        } else {
            throw new PersistenceException("The user couldn't be deleted");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void modifyUser(Users u) throws PersistenceException, Exception {
        try {
            if (u.getId() != null) {
                em.merge(u);
            } else {
                em.persist(u);
            }
        } catch (PersistenceException e) {
            throw new EntityNotFoundException("The user: " + u.getUsername() + " was not found.");
        } catch (Exception e) {
            throw new Exception("Something happened", e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Token">
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addToken(Token t) throws Exception {
        try {
            em.persist(t);
        } catch (Exception e) {
            throw new Exception("Unexpected error", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteToken(Token t) throws Exception {
        // if(t.getId() != null)
        t = em.merge(t);
        em.remove(t);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Token getToken(String t) throws EJBException {
        try {
            return (Token) em.createNamedQuery("getToken").setParameter("token", t).getSingleResult();
        } catch (NoResultException e) {
            throw new EJBException("Token " + t + " not found");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Token> getUserTokens(Users u) throws Exception {
        try {

            List<Token> tokenLst = em.createNamedQuery("getUserTokens").setParameter("idUser", u).getResultList();
            return tokenLst;
        } catch (Exception e) {
            throw new Exception("Not found ");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteUserTokens(Users user) throws Exception {
        try {
            List<Token> userTokenList = this.getUserTokens(user);
            for (int i = 0; i < userTokenList.size(); i++) {
                this.deleteToken(userTokenList.get(i));

            }
        } catch (Exception e) {
            throw new Exception("deleteUserTokens error");
        }
    }

   //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Booking">
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addBooking(Booking booking) throws Exception {
        try {

            em.persist(booking);
        } catch (Exception e) {
            throw new Exception("Booking already exists", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void modifyBooking(Booking booking) throws PersistenceException, Exception {
        try {
            if (booking.getId() != null) {
                em.merge(booking);
            } else {
                em.persist(booking);
            }
        } catch (PersistenceException e) {
            throw new EntityNotFoundException("The booking for the user: " + booking.getUser() + " was not found.");
        } catch (Exception e) {
            throw new Exception("Unexpected error while updating booking occur. Please try again.", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteBooking(Booking booking) throws PersistenceException, Exception {
        if (booking.getId() != null) {

            em.remove(booking);
        } else {
            throw new PersistenceException("The user couldn't be deleted");
        }
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Booking> getBookingsByUser(Users u) throws PersistenceException{
        try{
            if(u.getId() != null){
                return (List<Booking>) em.createNamedQuery("getBookingsByUser").setParameter("idUsers", u).getResultList();
                                                                                             
            }
            else{
                return null;
            }
        }
        catch(PersistenceException pEx){
            String aux = pEx.getMessage();
            return null;
        }
         
    }
    
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Booking getOneBooking(Booking booking) throws PersistenceException{
        try{
            return (Booking) em.createNamedQuery("getOneBooking").
                    setParameter("idUser", booking.getUser()).
                    setParameter("dateInit", booking.getInitialDate(),TemporalType.DATE).
                    getSingleResult();
        }
        catch(PersistenceException pEx){
            return null;
        }
    }
    
   //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Books">
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addBook(Books book) throws Exception {
        try {

            em.persist(book);
        } catch (Exception e) {
            throw new Exception("Booking already exists", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Books> getBooksByAuthor(String author) throws Exception {
        try {
            return (List<Books>) em.createNamedQuery("getBooksByAuthor").setParameter("author", author).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Books> getBooksByTitle(String title) throws Exception {
        try {
            return (List<Books>) em.createNamedQuery("getBooksByTitle").setParameter("title", title).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Books getBook(String isbn) throws Exception {
        try {
            return (Books) em.createNamedQuery("getBook").setParameter("isbn", isbn).getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Books> getAllBooks() {
        try {
            return em.createNamedQuery("getAllBooks").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Messages">
    @Override
    public void addMessage(Messages message) throws Exception {
        try {
            em.persist(message);
        } catch (Exception e) {
            throw new Exception("Could not save Message", e);
        }
    }

    @Override
    public List<Messages> getAllMessage() throws Exception {
        try {
            return em.createNamedQuery("getAllMessages").getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Messages> getMessage(String user) throws Exception {
        try {
            Users u = this.getUser(user);
            return em.createNamedQuery("getMessages").setParameter("idUser", u).getResultList();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void messageSeen(Messages message) throws Exception {
        try {
            if(message.getId() != null) {
                em.merge(message);
            } else {
                em.persist(message);
            }
                
        } catch (Exception e) {

        }
    }
    //</editor-fold>

}
