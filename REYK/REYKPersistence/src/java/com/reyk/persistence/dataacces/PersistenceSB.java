/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.dataacces;

import com.reyk.persistence.entities.Users;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

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
}
