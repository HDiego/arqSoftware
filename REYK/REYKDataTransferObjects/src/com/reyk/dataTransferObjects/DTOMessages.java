/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.dataTransferObjects;

/**
 *
 * @author diegorocca
 */
public class DTOMessages {
    private Long id;
    private DTOUsers user;
    private boolean seen;
    private String msj;

    public DTOMessages(DTOUsers user, boolean seen, String msj) {
        this.user = user;
        this.seen = seen;
        this.msj = msj;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public DTOUsers getUser() {
        return user;
    }

    public void setUser(DTOUsers user) {
        this.user = user;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getMsj() {
        return msj;
    }

    public void setMsj(String msj) {
        this.msj = msj;
    }
    
    
}
