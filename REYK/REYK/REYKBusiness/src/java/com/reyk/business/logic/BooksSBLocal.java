/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.persistence.entities.Books;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author MacAA
 */
@Local
public interface BooksSBLocal {
    
    void addBook(DTOBooks dtoBook) throws Exception;
    List<DTOBooks> getBooksByAuthor(String author) throws Exception;
    List<DTOBooks> getBooksByTitle(String title) throws Exception;
    DTOBooks getBook(String isbn) throws Exception;
    boolean existsBook(String isbn) throws Exception;
    List<DTOBooks> getAllBooks() throws Exception;
}
