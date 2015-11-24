/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.dataTransferObjects.DTOMessages;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Messages;
import com.reyk.persistence.entities.Users;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;

/**
 *
 * @author diegorocca
 */
@Stateless
public class MessagesSB implements MessagesSBLocal {

    @EJB
    private PersistenceSBLocal persistenceSB;
    
    @EJB
    private TransformEntityToDTOLocal transformEntityToDtoSB;
    
    @EJB
    private TransformDtoToEntityLocal transformDtoToEntitySB;

    @Override
    public void addMessages(DTOMessages dtoMessage) throws Exception {
        Messages message = transformDtoToEntitySB.transformDTOMessagesToMessages(dtoMessage);
        persistenceSB.addMessage(message);
    }
    
    @Override
    public List<DTOMessages> getMessages(String user) {
        try
        {
            List<DTOMessages> listDTO = transformEntityToDtoSB.transformListMessagesToListDTO(persistenceSB.getMessage(user));
            return listDTO;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    @Override
    public void messageSeen(DTOMessages message) throws Exception {
        Messages mes = transformDtoToEntitySB.transformDTOMessagesToMessages(message);
        mes.getUser().setId(persistenceSB.getUser(mes.getUser().getUsername()).getId());
       mes.setId(message.getId());
        mes.setSeen(true);
        persistenceSB.messageSeen(mes);
    }
    
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    
}
