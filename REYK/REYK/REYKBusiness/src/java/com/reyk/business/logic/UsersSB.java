/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.business.exception.EntityExistException;
import com.reyk.business.exception.EntityIncorrectAddException;
import com.reyk.business.exception.EntityIncorrectUpdateException;
import com.reyk.business.exception.EntityNotExistException;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Token;
import com.reyk.persistence.entities.Users;
import com.reyk.socialmedia.interfaces.SocialMediaSBLocal;
import java.util.ArrayList;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author MacAA
 */
@Stateless
public class UsersSB implements UsersSBLocal {

    @EJB
    private SingletonSBLocal singletonSB;

    @EJB
    private PersistenceSBLocal persistenceSB;

    @EJB
    private TransformEntityToDTOLocal transformEntityToDtoSB;

    @EJB
    private TransformDtoToEntityLocal transformDtoToEntitySB;

    @EJB(beanName = "TwitterSB")//Default session bean name for Twitter4j
    private SocialMediaSBLocal socialMedia;

    //<editor-fold defaultstate="collapsed" desc="Users">
    @Override
    public void updateUser(DTOUsers newUserDto) throws Exception {
        try {
            if (this.exists(newUserDto.getUsername())) {
                Users _user = transformDtoToEntitySB.transformDTOUserToUser(newUserDto);
                _user.setId(persistenceSB.getUser(_user.getUsername()).getId());
                persistenceSB.modifyUser(_user);
            }
        } catch (EJBException e) {
            throw new EntityIncorrectUpdateException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public DTOUsers getUser(String username) throws Exception {
        try {
            Users u = persistenceSB.getUser(username);
            DTOUsers dto = transformEntityToDtoSB.transformUserToDTOUsers(u);
            return dto;
        } catch (EJBException e) {
            throw new EntityNotExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public boolean exists(String username) throws EntityNotFoundException, Exception {
        try {
            boolean exists = persistenceSB.getUser(username) != null;
            return exists;
        } catch (EJBException e) {
            return false;
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public void deleteUser(String username) throws Exception {
        try {
            if (exists(username)) {
                Users userToDelete = persistenceSB.getUser(username);
                persistenceSB.deleteUser(userToDelete);
            }
        } catch (EJBException e) {
            throw new EntityNotExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public void addUser(DTOUsers userDto) throws Exception {
        try {
            Users u = transformDtoToEntitySB.transformDTOUserToUser(userDto);
            persistenceSB.addUser(u);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public List<DTOUsers> getUsers() throws Exception {
        try {
            List<DTOUsers> listDTO = transformEntityToDtoSB.transformListUserToListDTO(persistenceSB.getUsers());
            for(int i = 0; i< listDTO.size(); i++) {
                listDTO.get(i).setId(null);
            }
            return listDTO;
        } catch (EJBException e) {
            return new ArrayList<DTOUsers>();
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Login/Logout">
    @Override
    public String login(String token, String username, String password) throws Exception {
        try {
            Users _user = persistenceSB.getUser(username);
            if (this.isValidToken(token, username)) {
                if (_user.getPassword().equals(password)) {
                    return token;
                } else {
                    return "Username and password did not match";
                }
            } else {
                return "Invalid login";
            }
        } catch (EJBException e) {
            throw new EntityNotExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public String logout(String token) throws Exception {
        try {
            Token t = persistenceSB.getToken(token);
            persistenceSB.deleteToken(t);
            return "Logout successfull";
        } catch (EJBException e) {
            throw new EntityExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isLoggedIn(String username, String token) throws Exception {
        try {
            Token tokenT = persistenceSB.getToken(token);
            return tokenT.getUser().getUsername().equals(username);
        } catch (EJBException e) {
            return false;
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    /* Returns a token for a user. Is matches a username and a token. 
     AUTHENTICATION
     */
    @Override
    public String authenticatorToken(String username, String password) throws Exception {
        String tokenString = "";
        try {
            Users u = persistenceSB.getUser(username);
            if(u.getPassword().equals(password))
            {
                Calendar calendar = Calendar.getInstance();
                tokenString = UUID.randomUUID() + "#" + calendar.getTimeInMillis();
                Token token = new Token();
                token.setToken(tokenString);
                token.setUser(u);
                persistenceSB.addToken(token);
                return tokenString;
            }
            throw new EntityIncorrectAddException("The username and password did not match");
        } catch (EJBException e) {
            throw new EntityExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityExistException(e.getMessage(), e);
        }
    }

    private boolean isValidToken(String token, String username) throws Exception {
        try {
            Users u = persistenceSB.getUser(username);
            Token tokenDB = persistenceSB.getToken(token);
            List<Token> tokenLst = persistenceSB.getUserTokens(u);
            for (int i = 0; i < tokenLst.size(); i++) {
                if (token.equals(tokenLst.get(i).getToken())) {
                    return true;
                }
            }
            return false;
        } catch (EJBException e) {
            return false;
        } catch (Exception e) {
            throw new EntityExistException(e.getMessage(), e);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Social Media for Twitter">
    @Override
    public String connectToSocialMedia(String _socialMedia, String username) {
        try {
            //Looking up for the specific SB.
            socialMedia = (SocialMediaSBLocal) InitialContext.doLookup("java:global/REYK/REYKSocialMedia/" + _socialMedia + "SB");
            return socialMedia.connectToSocialMedia(username);
        } catch (NamingException namingEx) {
            return "That social media is not available in REYK.";
        }
    }

    @Override
    public String disconnectFromSocialMedia(String _socialMedia, String username) {
        try {
            //Looking up for the specific SB.
            socialMedia = (SocialMediaSBLocal) InitialContext.doLookup("java:global/REYK/REYKSocialMedia/" + _socialMedia + "SB");
        } catch (NamingException namingEx) {
            return "Error connecting to " + _socialMedia;
        }
        return socialMedia.disconnectFromSocialMedia(username);
    }

    @Override
    public void addPin(String pin, String username) {
        socialMedia.addPin(pin, username);
    }

    @Override
    public void postComment(String post, String username, String _socialMedia) {
        try {
            socialMedia = (SocialMediaSBLocal) InitialContext.doLookup("java:global/REYK/REYKSocialMedia/" + _socialMedia + "SB");
            socialMedia.postComment(username, post);
        } catch (NamingException ex) {
            //No hay nada que podamos hacer para corregirlo.
        }
    }

    //</editor-fold>
}
