/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.entities.Booking;
import java.util.List;
import javax.ejb.Local;
import javax.persistence.PersistenceException;

/**
 *
 * @author MacAA
 */
@Local
public interface BookingsSBLocal {
    
    void addBooking(DTOBookings booking) throws Exception;
    void modifyBooking(DTOBookings booking) throws PersistenceException, Exception;
    void deleteBooking(DTOBookings booking) throws PersistenceException, Exception;
    List<DTOBookings> getBookingsByUser(DTOUsers u) throws PersistenceException, Exception;
    Booking getOneBooking(DTOBookings booking) throws PersistenceException;
}
