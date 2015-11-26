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
public class EntityNotExistException extends Exception{

    public EntityNotExistException() {
        super();
    }

    public EntityNotExistException(String message) {
        super(message);
    }

    public EntityNotExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityNotExistException(Throwable cause) {
        super(cause);
    }
    
}
