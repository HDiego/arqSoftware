/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Booking;
import com.reyk.persistence.entities.Users;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.PersistenceException;

/**
 *
 * @author MacAA
 */
@Stateless
public class BookingsSB implements BookingsSBLocal {

    @EJB
    private PersistenceSBLocal persistenceSB;

    @EJB
    private TransformEntityToDTOLocal transformEntityToDtoSB;

    @EJB
    private TransformDtoToEntityLocal transformDtoToEntitySB;

    @Override
    public void addBooking(DTOBookings booking) throws Exception {
        Booking _booking = this.transformDtoToEntitySB.transformDTOBookingsToBooking(booking);
        _booking.getUser().setId(persistenceSB.getUser(_booking.getUser().getUsername()).getId());
        _booking.getBooks().setId(persistenceSB.getBook(_booking.getBooks().getIsbn()).getId());
        persistenceSB.addBooking(_booking);

    }

    @Override
    public void modifyBooking(DTOBookings booking) throws PersistenceException, Exception {
        try {
            Booking _booking = this.transformDtoToEntitySB.transformDTOBookingsToBooking(booking);
            persistenceSB.modifyBooking(_booking);
        } catch (PersistenceException pEx) {
            throw new PersistenceException(pEx.getMessage());
        }
    }

    @Override
    public void deleteBooking(DTOBookings booking) throws PersistenceException, Exception {
        Booking _booking = this.transformDtoToEntitySB.transformDTOBookingsToBooking(booking);
        persistenceSB.deleteBooking(_booking);
    }

    @Override
    public List<DTOBookings> getBookingsByUser(DTOUsers u) throws PersistenceException, Exception {
        try {
            
            Users _u = persistenceSB.getUser(u.getUsername());
            if (_u.getId() != null) {
                List<Booking> lstBooking = persistenceSB.getBookingsByUser(_u);
                if (lstBooking != null) {
                    List<DTOBookings> returnLst = new ArrayList<DTOBookings>();
                    for (int i = 0; i < lstBooking.size(); i++) {
                        DTOBookings dto = this.transformEntityToDtoSB.transformBookingToDTOBookings(lstBooking.get(i));
                        returnLst.add(dto);
                    }
                    return returnLst;
                }
                return null;
            }
        } 
        catch(PersistenceException pEx){
            return null;
        }
        catch(Exception eX){
            String aux = eX.getMessage();
            throw new Exception(eX.getMessage());
        }
        return null;
    }
    
    @Override
    public Booking getOneBooking(DTOBookings booking) throws PersistenceException{
        try{
            Booking _booking = this.transformDtoToEntitySB.transformDTOBookingsToBooking(booking);
            _booking.getUser().setId(persistenceSB.getUser(_booking.getUser().getUsername()).getId());
            _booking.getBooks().setId(persistenceSB.getBook(_booking.getBooks().getIsbn()).getId());
            
            return persistenceSB.getOneBooking(_booking);
            
        }
        catch(PersistenceException pEx){
            return null;
        }
        catch(Exception e){
            return null;
        }
    }

}
