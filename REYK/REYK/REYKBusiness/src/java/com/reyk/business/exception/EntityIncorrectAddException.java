/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.exception;

/**
 *
 * @author diegorocca
 */
public class EntityIncorrectAddException extends Exception {

    public EntityIncorrectAddException() {
        super();
    }

    public EntityIncorrectAddException(String message) {
        super(message);
    }

    public EntityIncorrectAddException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityIncorrectAddException(Throwable cause) {
        super(cause);
    }
    
}
