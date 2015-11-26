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
public class EntityExistException extends Exception {

    public EntityExistException() {
        super();
    }

    public EntityExistException(String message) {
        super(message);
    }

    public EntityExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityExistException(Throwable cause) {
        super(cause);
    }    
}
