/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformUserSBLocal;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Token;
import com.reyk.persistence.entities.Users;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import javax.ejb.EJB;
import javax.ejb.Stateless;

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
    private TransformUserSBLocal transformUserSB;
    
    //<editor-fold defaultstate="collapsed" desc="Users">
    
    @Override
    public void updateUser(DTOUsers newUserDto) throws Exception 
    {
        if (this.exists(newUserDto.getUsername())) 
        {
            Users _user = transformUserSB.transformDTOUserToUser(newUserDto);
            _user.setId(this.getUser(_user.getUsername()).getId());
            
            persistenceSB.modifyUser(_user);
        }
    }

    @Override
    public DTOUsers getUser(String username) {
        try {
            Users u = persistenceSB.getUser(username);
            DTOUsers dto = transformUserSB.transformUserToDTOUsers(u);
            return dto;
        } catch (Exception e) {
           
            return null;
        }
    }

    @Override
    public boolean exists(String username) throws Exception {
        
        try{
            return persistenceSB.getUser(username) != null;
        }
        catch(Exception e){
            throw new Exception("The user doesn't exists");
        }
    }

    @Override
    public void deleteUser(String username) throws Exception{
        if (exists(username)) {
            Users userToDelete = persistenceSB.getUser(username);
            persistenceSB.deleteUser(userToDelete);
        }
    }

    @Override
    public void addUser(DTOUsers userDto) throws Exception {
        Users u = new Users();
        u.setName(userDto.getName());
        u.setSurname(userDto.getSurname());
        u.setEmail(userDto.getEmail());
        u.setUsername(userDto.getUsername());
        u.setPassword(userDto.getPassword());

        persistenceSB.addUser(u);
        singletonSB.AddUser(userDto);
    }

    @Override
    public List<DTOUsers> getUsers() {

        return singletonSB.getUsers();
    }

    //</editor-fold>
    
    @Override
    public String login(String token, String username, String password) throws Exception {
      
        try {
                
            Users _user = persistenceSB.getUser(username);
            if(this.isValidToken(token, username)){
                if(_user.getPassword().equals(password)){
                    return token;
                }
                else{
                    return "Username and password did not match";
                }
            }
            else{
                return "Invalid login";
            }
          
        } catch (Exception e) {
            throw new Exception("There was an error");
        }

    }
  

    @Override
    public String logout(String token) throws Exception {
        try{
            Token t = persistenceSB.getToken(token);
            persistenceSB.deleteToken(t);
            return "Logout successfull";
        }
        catch(Exception e){
            throw new Exception("Error while logingout");
        }
    }
    
    @Override
    public boolean isLoggedIn(String username, String token){
        try{
            Token tokenT = persistenceSB.getToken(token);
            return tokenT.getUser().getUsername() == username;
        }
        catch(Exception e){
            return false;
        }
    }
    
    /* Returns a token for a user. Is matches a username and a token. 
        AUTHENTICATION
    */
    @Override
    public String authenticatorToken(String username) throws Exception{
        
        String tokenString = "";
        
        try {
           
            Users u = persistenceSB.getUser(username);
            
            Calendar calendar = Calendar.getInstance();
            tokenString = UUID.randomUUID() + "#" + calendar.getTimeInMillis();
            Token token = new Token();
            token.setToken(tokenString);
            token.setUser(u);
            persistenceSB.addToken(token);

            return tokenString;
        } catch (Exception e) {
            throw new Exception("There was an error");
        }
    }
    
    private boolean isValidToken(String token, String username) throws Exception{
        
        try{
            
            Users u = persistenceSB.getUser(username);
            Token tokenDB = persistenceSB.getToken(token);
            List<Token> tokenLst = persistenceSB.getUserTokens(u);
            for (int i = 0; i < tokenLst.size(); i++) {
                if(token.equals(tokenLst.get(i).getToken()))
                    return true;
            }
            return false;
            
        }
        catch(Exception e){
            throw new Exception("IsValidToken Exception");
        }
        
        
    }
    
}


