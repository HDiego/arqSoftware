/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.dtoTransformer;

import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.entities.Users;
import javax.ejb.Stateless;

/**
 *
 * @author diegorocca
 */
@Stateless
public class TransformUserSB implements TransformUserSBLocal {

    @Override
    public DTOUsers transformUserToDTOUsers(Users u) {
        DTOUsers dtoU = new DTOUsers(u.getName(),u.getSurname(),u.getUsername(),u.getPassword(),u.getEmail());
        return dtoU;
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public Users transformDTOUserToUser(DTOUsers dtUser) {
        Users user = new Users();
        
        user.setName(dtUser.getName());
        user.setSurname(dtUser.getSurname());
        user.setEmail(dtUser.getEmail());
        user.setUsername(dtUser.getUsername());
        user.setPassword(dtUser.getPassword());
        
        return user;
        
    }
}