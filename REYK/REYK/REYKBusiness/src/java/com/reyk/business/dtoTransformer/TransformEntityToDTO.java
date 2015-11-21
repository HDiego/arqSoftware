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
import javax.ejb.Stateless;

/**
 *
 * @author diegorocca
 */
@Stateless
public class TransformEntityToDTO implements TransformEntityToDTOLocal {

    @Override
    public DTOUsers transformUserToDTOUsers(Users u) {
        DTOUsers dtoU = new DTOUsers(u.getName(),u.getSurname(),u.getUsername(),u.getPassword(),u.getEmail());
        return dtoU;
    }
 
    @Override
    public DTOBooks transformBookToDTOBooks(Books book){
        DTOBooks dtoBooks = new DTOBooks(book.getAuthor(), 
                                         book.getTitle(), 
                                         book.getIsbn(), 
                                         book.getGenre(), 
                                         book.getPrice());
        return dtoBooks;
    }
    
    @Override
    public DTOBookings transformBookingToDTOBookings(Booking booking){
        
        DTOUsers dtoUsers = this.transformUserToDTOUsers(booking.getUser());
        DTOBooks dtoBooks = this.transformBookToDTOBooks(booking.getBooks());
        DTOBookings dtoBooking = new DTOBookings(dtoUsers, 
                                                dtoBooks, 
                                                booking.getInitialDate(), 
                                                booking.getFinalDate());
        
        return dtoBooking;
        
    }
    
}
