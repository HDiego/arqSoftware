/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.business.exception.EntityNotExistException;
import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Booking;
import com.reyk.persistence.entities.Users;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.ejb.Stateless;

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
        try {
            Booking _booking = this.transformDtoToEntitySB.transformDTOBookingsToBooking(booking);
            _booking.getUser().setId(persistenceSB.getUser(_booking.getUser().getUsername()).getId());
            _booking.getBooks().setId(persistenceSB.getBook(_booking.getBooks().getIsbn()).getId());
            persistenceSB.addBooking(_booking);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public List<DTOBookings> getBookingsByUser(DTOUsers u) throws Exception {
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
            return null;
        } catch (EJBException e) {
            throw new EntityNotExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }
}
