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
public class EntityIncorrectUpdateException extends Exception {

    public EntityIncorrectUpdateException() {
        super();
    }

    public EntityIncorrectUpdateException(String message) {
        super(message);
    }

    public EntityIncorrectUpdateException(String message, Throwable cause) {
        super(message, cause);
    }

    public EntityIncorrectUpdateException(Throwable cause) {
        super(cause);
    }
    
}
