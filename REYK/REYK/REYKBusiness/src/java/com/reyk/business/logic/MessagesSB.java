/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.business.exception.EntityIncorrectUpdateException;
import com.reyk.business.exception.EntityNotExistException;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.dataTransferObjects.DTOMessages;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Messages;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
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
        try {
            Messages message = transformDtoToEntitySB.transformDTOMessagesToMessages(dtoMessage);
            persistenceSB.addMessage(message);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public List<DTOMessages> getMessages(String user) throws Exception {
        try {
            List<Messages> lisMes = persistenceSB.getMessage(user);
            List<DTOMessages> listDTO = transformEntityToDtoSB.transformListMessagesToListDTO(lisMes);
            return listDTO;
        } catch (EJBException e) {
            return new ArrayList<DTOMessages>();
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public void messageSeen(DTOMessages message) throws Exception {
        try {
            Messages mes = transformDtoToEntitySB.transformDTOMessagesToMessages(message);
            mes.getUser().setId(persistenceSB.getUser(mes.getUser().getUsername()).getId());
            mes.setId(message.getId());
            mes.setSeen(true);
            persistenceSB.messageSeen(mes);
        } catch (EJBException e) {
            throw new EntityIncorrectUpdateException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
}
