/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.dataTransferObjects.DTOUsers;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;

/**
 *
 * @author MacAA
 */
@Singleton
public class SingletonSB implements SingletonSBLocal {
    
    public List<DTOUsers> userList; // = new ArrayList<DTOUsers>();
    
    public SingletonSB(){
        
        userList = new ArrayList<DTOUsers>();
    }
//Hola
    @Override
    public DTOUsers getUserByUsename(String username) {
        for (int i = 0; i < userList.size(); i++) {
            DTOUsers dto = new DTOUsers(username);
            if(userList.get(i).equals(dto))
            {
                return userList.get(i);
            }
        }
        return null;
    }
    
    @Override
    public void AddUser(DTOUsers user) {
        userList.add(user);
    }

    @Override
    public List<DTOUsers> getUsers() {
        return userList;
    }

    
   
}
