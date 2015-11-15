/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.dataacces;

import com.mysql.jdbc.Statement;
import com.reyk.persistence.entities.Users;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.resource.cci.ResultSet;

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
    public Users getUser(String username) throws Exception {
        try {
           // Users us = (Users) em.find(Users.class,1);
           //Query q = em.createNamedQuery("select * from Users u where u.USERNAME =:name");
           //q.setParameter("name", username);
         return em.createQuery("SELECT u FROM Users u WHERE u.username = :usernames",Users.class).setParameter("usernames",username).getSingleResult();
            //String aux = a.getUsername();
//Users u = (Users)q.getSingleResult(); //em.createNamedQuery("getUser").setParameter("userName", username).getSingleResult();
          //  return null;
        } catch (Exception e) {
            throw new EJBException("No se encontró el usuario " + username, e);
        }
    }

}
