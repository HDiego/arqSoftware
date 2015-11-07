/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;



import com.reyk.dataTransferObjects.DTOUsers;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MacAA
 */
@Local
public interface UsersSBLocal {
 
    void addUser(DTOUsers userDto);
    List<DTOUsers> getUsers();
    void updateUser(DTOUsers newUserDto);
    DTOUsers getUser(String username);
    boolean exists(String username);
    void deleteUser(String username);
 
}