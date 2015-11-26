/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.business.exception.EntityNotExistException;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Books;
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
public class BooksSB implements BooksSBLocal {

    @EJB
    private PersistenceSBLocal persistenceSB;

    @EJB
    private TransformEntityToDTOLocal transformEntityToDtoSB;

    @EJB
    private TransformDtoToEntityLocal transformDtoToEntitySB;

    @Override
    public void addBook(DTOBooks dtoBook) throws Exception {
        try {
            Books book = transformDtoToEntitySB.transformDTOBooksToBooks(dtoBook);
            persistenceSB.addBook(book);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public List<DTOBooks> getBooksByAuthor(String author) throws Exception {
        try {
            List<Books> lstBooks = persistenceSB.getBooksByAuthor(author);
            List<DTOBooks> lstDTO = new ArrayList<DTOBooks>();
            for (int i = 0; i < lstBooks.size(); i++) {
                DTOBooks dto = this.transformEntityToDtoSB.transformBookToDTOBooks(lstBooks.get(i));
                lstDTO.add(dto);
            }
            return lstDTO;
        } catch (EJBException e) {
            return new ArrayList<DTOBooks>();
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public List<DTOBooks> getBooksByGenre(String genre) throws Exception {
        try {
            List<Books> lstBooks = persistenceSB.getBooksByGenre(genre);
            List<DTOBooks> lstDTO = new ArrayList<DTOBooks>();
            for (int i = 0; i < lstBooks.size(); i++) {
                DTOBooks dto = this.transformEntityToDtoSB.transformBookToDTOBooks(lstBooks.get(i));
                lstDTO.add(dto);
            }
            return lstDTO;
        } catch (EJBException e) {
            return new ArrayList<DTOBooks>();
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public DTOBooks getBook(String isbn) throws Exception {
        try {
            Books book = persistenceSB.getBook(isbn);
            DTOBooks dtoBook = this.transformEntityToDtoSB.transformBookToDTOBooks(book);
            return dtoBook;
        } catch (EJBException e) {
            throw new EntityNotExistException(e.getMessage(), e);
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public boolean existsBook(String isbn) throws Exception {
        try {
            boolean exist = persistenceSB.getBook(isbn) != null;
            return exist;
        } catch (EJBException e) {
            return false;
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }

    @Override
    public List<DTOBooks> getAllBooks() throws Exception {
        try {
            List<Books> lstBooks = persistenceSB.getAllBooks();
            List<DTOBooks> lstDto = new ArrayList<DTOBooks>();
            for (int i = 0; i < lstBooks.size(); i++) {
                DTOBooks dto = this.transformEntityToDtoSB.transformBookToDTOBooks(lstBooks.get(i));
                lstDto.add(dto);
            }
            return lstDto;
        } catch (EJBException e) {
            return new ArrayList<DTOBooks>();
        } catch (Exception e) {
            throw new EntityNotExistException(e.getMessage(), e);
        }
    }
}
