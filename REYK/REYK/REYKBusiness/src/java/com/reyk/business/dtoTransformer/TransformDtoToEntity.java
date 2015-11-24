/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.dtoTransformer;

import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.dataTransferObjects.DTOMessages;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.entities.Booking;
import com.reyk.persistence.entities.Books;
import com.reyk.persistence.entities.Messages;
import com.reyk.persistence.entities.Users;
import javax.ejb.Stateless;

/**
 *
 * @author MacAA
 */
@Stateless
public class TransformDtoToEntity implements TransformDtoToEntityLocal {

    @Override
    public Users transformDTOUserToUser(DTOUsers dtoUser) {
       
        Users user = new Users();
        user.setName(dtoUser.getName());
        user.setSurname(dtoUser.getSurname());
        user.setEmail(dtoUser.getEmail());
        user.setUsername(dtoUser.getUsername());
        user.setPassword(dtoUser.getPassword());
        user.setSuscribed(dtoUser.isSuscribed());
        return user;
        
    }
    
    @Override
    public Books transformDTOBooksToBooks(DTOBooks dtoBooks){
        
        Books book = new Books();
        book.setAuthor(dtoBooks.getAuthor());
        book.setGenre(dtoBooks.getGenre());
        book.setIsbn(dtoBooks.getIsbn());
        book.setPrice(dtoBooks.getPrice());
        book.setTitle(dtoBooks.getTitle());
        
        return book;
    }
    
    @Override
    public Booking transformDTOBookingsToBooking(DTOBookings dtoBookings){
        
        Booking booking = new Booking();
        Books books = this.transformDTOBooksToBooks(dtoBookings.getBooks());
        Users users = this.transformDTOUserToUser(dtoBookings.getUser());
        booking.setBooks(books);
        booking.setUser(users);
        booking.setInitialDate(dtoBookings.getInitialDate());
        booking.setFinalDate(dtoBookings.getFinalDate());
        
        return booking;
    }

    @Override
    public Messages transformDTOMessagesToMessages(DTOMessages dtoMes) {
        Messages message = new Messages();
        message.setMessage(dtoMes.getMsj());
        message.setSeen(dtoMes.isSeen());
        Users u = this.transformDTOUserToUser(dtoMes.getUser());
        u.setId(dtoMes.getUser().getId());
        message.setUser(u);
        return message;
    }
    
    
}
