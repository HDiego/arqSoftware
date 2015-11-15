/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.persistence.dataacces;

import com.reyk.persistence.entities.Users;
import javax.ejb.Local;

/**
 *
 * @author MacAA
 */
@Local
public interface PersistenceSBLocal {
    
    void addUser(Users u) throws Exception;
}
