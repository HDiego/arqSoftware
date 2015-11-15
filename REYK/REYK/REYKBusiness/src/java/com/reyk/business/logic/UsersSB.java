/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformUserSBLocal;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
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
    
    @Override
    public void updateUser(DTOUsers newUserDto) {
        if (this.exists(newUserDto.getUsername())) {
            singletonSB.getUsers().remove(newUserDto);
            singletonSB.getUsers().add(newUserDto);
        }
    }

    @Override
    public DTOUsers getUser(String username) {
        try {
            Users u = persistenceSB.getUser(username);
            DTOUsers dto = transformUserSB.transformUserToDTOUsers(u);
            return dto;
        } catch (Exception e) {
               
        }
        /*DTOUsers userAux = singletonSB.getUserByUsename(username);
        if (userAux != null) {
            return userAux;
        }*/
        return null;
    }

    @Override
    public boolean exists(String username) {
        if (singletonSB == null) {
            return true;
        }

        if (singletonSB.getUserByUsename(username) != null) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteUser(String username) {
        if (exists(username)) {
            DTOUsers userAux = new DTOUsers(username);
            singletonSB.getUsers().remove(userAux);
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

    @Override
    public String login(String username, String password) throws Exception {
        String token = "";
        try {
            DTOUsers u = singletonSB.getUserByUsename(username); //cambiar aca luego a persistencia y entidad User
            if (u.getPassword().trim() == password.trim()) {  //definir equals en entidades
                Calendar calendar = Calendar.getInstance();
                token = UUID.randomUUID() + "#" + calendar.getTimeInMillis();
                /*   Token t = new Token();
                 t.setToken(token);
                 //  t.setUser(u);
                 //agregar el token a la */
            }
            return token;
        } catch (Exception e) {
            throw new Exception("There was an error");
        }

    }

    @Override
    public String logout(String token) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
