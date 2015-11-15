/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.dtoTransformer;

import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.entities.Users;
import javax.ejb.Local;

/**
 *
 * @author diegorocca
 */
@Local
public interface TransformUserSBLocal {
    DTOUsers transformUserToDTOUsers(Users u);
    Users transformDTOUserToUser(DTOUsers dtUser);
}
