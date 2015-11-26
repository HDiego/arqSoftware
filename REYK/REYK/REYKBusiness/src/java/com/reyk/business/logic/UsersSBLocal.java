/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;



import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.entities.Users;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.EntityNotFoundException;

/**
 *
 * @author MacAA
 */
@Local
public interface UsersSBLocal {
 
    void addUser(DTOUsers userDto) throws Exception;
    List<DTOUsers> getUsers()throws Exception;
    void updateUser(DTOUsers newUserDto) throws Exception;
    DTOUsers getUser(String username)throws Exception;
    boolean exists(String username) throws EntityNotFoundException, Exception;
    void deleteUser(String username) throws Exception;
    
    String login(String token, String username, String password) throws Exception;
    String logout(String token) throws Exception;
    boolean isLoggedIn(String username, String token) throws Exception;
    String authenticatorToken(String username, String password) throws Exception;
    
    String connectToSocialMedia(String _socialMedia, String username);
    String disconnectFromSocialMedia(String _socialMedia, String username);
    void addPin(String pin, String username);
    void postComment(String post, String username, String _socialMedia);
}
