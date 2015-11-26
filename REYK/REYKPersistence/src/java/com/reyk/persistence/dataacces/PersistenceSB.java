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
import javax.persistence.EntityExistsException;
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
            throw new EntityExistsException("Unknown Error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users getUser(String username) throws Exception {
        try {
            return (Users) em.createNamedQuery("getUser").setParameter("user", username).getSingleResult();
        } catch (NoResultException e) {
            throw new EJBException("The user " + username + " was not found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Users getUser(Long id) throws Exception {
        try {
            return em.find(Users.class, id);
        } catch (NoResultException e) {
            throw new EJBException("The user with the id " + id + " could not be found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    public List<Users> getUsers() throws Exception {
        try {
            List<Users> listUser = (List<Users>) em.createNamedQuery("getAllUsers").getResultList();
            return listUser;
        } catch (NoResultException e) {
            throw new EJBException("No users found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteUser(Users user) throws PersistenceException, Exception {
        try {
            if (user.getId() != null) {
                user = em.merge(user);
                this.deleteUserTokens(user);
                em.remove(user);
            } else {
                throw new PersistenceException("The user couldn't be deleted");
            }
        } catch (NoResultException e) {
            throw new EJBException("The user " + user.getUsername() + " could not be found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
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
        } catch (NoResultException e) {
            throw new EJBException("The user: " + u.getUsername() + " was not found.");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
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
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteToken(Token t) throws Exception {
        try {
            t = em.merge(t);
            em.remove(t);
        } catch (NoResultException e) {
            throw new EJBException("Could not logout");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Token getToken(String t) throws EJBException, Exception {
        try {
            return (Token) em.createNamedQuery("getToken").setParameter("token", t).getSingleResult();
        } catch (NoResultException e) {
            throw new EJBException("Token " + t + " was not found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Token> getUserTokens(Users u) throws Exception {
        try {
            List<Token> tokenLst = em.createNamedQuery("getUserTokens").setParameter("idUser", u).getResultList();
            return tokenLst;
        } catch (NoResultException e) {
            throw new EJBException("There was no result", e);
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later");
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
        } catch (NoResultException e) {
            throw new EJBException("The user " + user.getUsername() + "coud not be found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Booking">
    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void addBooking(Booking booking) throws Exception {
        try {
            em.persist(booking);
        } catch (PersistenceException e) {
            throw new EntityExistsException("Booking already exists", e);
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
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
            throw new Exception("Unexpected error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void deleteBooking(Booking booking) throws PersistenceException, Exception {
        try {
            if (booking.getId() != null) {
                em.remove(booking);
            } else {
                throw new PersistenceException("The booking couldn't be deleted");
            }
        } catch (PersistenceException e) {
            throw new EntityNotFoundException("The booking could not be deleted");
        } catch (Exception e) {
            throw new EntityNotFoundException("Unknown error, try again later");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Booking> getBookingsByUser(Users u) throws PersistenceException, Exception {
        try {
            if (u.getId() != null) {
                return (List<Booking>) em.createNamedQuery("getBookingsByUser").setParameter("idUsers", u).getResultList();
            } else {
                return null;
            }
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No bookings found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Booking getOneBooking(Booking booking) throws PersistenceException, Exception {
        try {
            return (Booking) em.createNamedQuery("getOneBooking").
                    setParameter("idUser", booking.getUser()).
                    setParameter("dateInit", booking.getInitialDate(), TemporalType.DATE).
                    getSingleResult();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No bookings found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
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
            throw new EntityExistsException("Unknown Error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Books> getBooksByAuthor(String author) throws Exception {
        try {
            List<Books> listBooks = (List<Books>) em.createNamedQuery("getBooksByAuthor").setParameter("author", author).getResultList();
            return listBooks;
        } catch (NoResultException e) {
            throw new EJBException("No books found written by: " + author);
        } catch (Exception e) {
            throw new Exception("Unkonwn error, try again later");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Books> getBooksByGenre(String genre) throws Exception {
        try {
            List<Books> listBook = (List<Books>) em.createNamedQuery("getBooksByGenre").setParameter("genre", genre).getResultList();
            return listBook;
        } catch (NoResultException e) {
            throw new EJBException("No books found with the genre: " + genre);
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later");
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Books getBook(String isbn) throws Exception {
        try {
            Books book = (Books) em.createNamedQuery("getBook").setParameter("isbn", isbn).getSingleResult();
            return book;
        } catch (NoResultException e) {
            throw new EJBException("The book with isbn: " + isbn + " could not be found");
        } catch (Exception e) {
            throw new Exception("Unkonwn error, try again later", e);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<Books> getAllBooks() throws Exception {
        try {
            List<Books> listBook = em.createNamedQuery("getAllBooks").getResultList();
            return listBook;
        } catch (NoResultException e) {
            throw new EJBException("No books found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Messages">
    @Override
    public void addMessage(Messages message) throws Exception {
        try {
            message.setUser(this.getUser(message.getUser().getUsername()));
            em.persist(message);
        } catch (PersistenceException e) {
            throw new EntityExistsException("Message already exists", e);
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later", e);
        }
    }

    @Override
    public List<Messages> getAllMessage() throws Exception {
        try {
            return em.createNamedQuery("getAllMessages").getResultList();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No messages found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later");
        }
    }

    @Override
    public List<Messages> getMessage(String user) throws Exception {
        try {
            Users u = this.getUser(user);
            return em.createNamedQuery("getMessages").setParameter("idUser", u).getResultList();
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No messages found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later");
        }
    }

    @Override
    public void messageSeen(Messages message) throws Exception {
        try {
            if (message.getId() != null) {
                em.merge(message);
            } else {
                em.persist(message);
            }
        } catch (NoResultException e) {
            throw new EntityNotFoundException("No messages found");
        } catch (Exception e) {
            throw new Exception("Unknown error, try again later");
        }
    }
    //</editor-fold>

}
