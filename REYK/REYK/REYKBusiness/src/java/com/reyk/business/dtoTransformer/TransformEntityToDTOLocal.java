/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.dtoTransformer;

import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.entities.Booking;
import com.reyk.persistence.entities.Books;
import com.reyk.persistence.entities.Users;
import javax.ejb.Local;

/**
 *
 * @author diegorocca
 */
@Local
public interface TransformEntityToDTOLocal {
    DTOUsers transformUserToDTOUsers(Users u);
    DTOBooks transformBookToDTOBooks(Books book);
    DTOBookings transformBookingToDTOBookings(Booking booking);
    
}