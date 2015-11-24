/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.dataTransferObjects.DTOMessages;
import com.reyk.dataTransferObjects.DTOUsers;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author diegorocca
 */
@Local
public interface MessagesSBLocal {
    void addMessages(DTOMessages message) throws Exception;
    List<DTOMessages> getMessages(String user);
    void messageSeen(DTOMessages message) throws Exception;
}
