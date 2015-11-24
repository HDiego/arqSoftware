/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.socialmedia.interfaces;

import javax.ejb.EJBException;
import javax.ejb.Local;

/**
 *
 * @author MacAA
 */
@Local
public interface SocialMediaSBLocal {
 
    String connectToSocialMedia(String username);
    String disconnectFromSocialMedia(String username);    
    void addPin(String pin, String username) throws EJBException;
    void postComment(String username,String post) throws EJBException;
    
}
