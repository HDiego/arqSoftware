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

/**
 *
 * @author MacAA
 */
@Local
public interface UsersSBLocal {
 
    void addUser(DTOUsers userDto) throws Exception;
    List<DTOUsers> getUsers();
    void updateUser(DTOUsers newUserDto) throws Exception;
    DTOUsers getUser(String username);
    boolean exists(String username) throws Exception;
    void deleteUser(String username) throws Exception;
    
    String login(String token, String username, String password) throws Exception;
    String logout(String token) throws Exception;
    boolean isLoggedIn(String username, String token);
    String authenticatorToken(String username) throws Exception;
    
    String connectToSocialMedia(String _socialMedia, String username);
    String disconnectFromSocialMedia(String _socialMedia, String username);
    void addPin(String pin, String username);
    void postComment(String post, String username, String _socialMedia);
}
