/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;


import com.reyk.dataTransferObjects.DTOUsers;
import java.util.ArrayList;
import java.util.List;
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

    @Override
    public void updateUser(DTOUsers newUserDto) {
        if(this.exists(newUserDto.getUsername()))
        {
            singletonSB.getUsers().remove(newUserDto);
            singletonSB.getUsers().add(newUserDto);
        }
    }

    @Override
    public DTOUsers getUser(String username) {
        DTOUsers userAux = singletonSB.getUserByUsename(username);
        if(userAux != null)
        {
            return userAux;
        }
        return null;
    }

    @Override
    public boolean exists(String username) {
        
        if(singletonSB.getUserByUsename(username) != null)
        {
            return true;
        }
        return false;
    }

    @Override
    public void deleteUser(String username) {
        if(exists(username))
        {
            DTOUsers userAux = new DTOUsers(username);
            singletonSB.getUsers().remove(userAux);
        }
    }
    
    @Override
    public void addUser(DTOUsers userDto) {
       
       singletonSB.AddUser(userDto);
    }

    @Override
    public List<DTOUsers> getUsers() {
       
        return singletonSB.getUsers();
    }
}