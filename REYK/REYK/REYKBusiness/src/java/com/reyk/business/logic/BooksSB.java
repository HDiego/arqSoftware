/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.business.logic;

import com.reyk.business.dtoTransformer.TransformDtoToEntityLocal;
import com.reyk.business.dtoTransformer.TransformEntityToDTOLocal;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.persistence.dataacces.PersistenceSBLocal;
import com.reyk.persistence.entities.Books;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
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
    public void addBook(DTOBooks dtoBook) throws Exception{
        
        try{
            Books book = transformDtoToEntitySB.transformDTOBooksToBooks(dtoBook);
            
            persistenceSB.addBook(book);
            
        }
        catch(Exception e){
            throw new Exception("The book couldn't be added");
        }
    }
    
    @Override
    public List<DTOBooks> getBooksByAuthor(String author) throws Exception{
        try{
            List<Books> lstBooks = persistenceSB.getBooksByAuthor(author);
            List<DTOBooks> lstDTO = new ArrayList<DTOBooks>(); 
            for (int i = 0; i < lstBooks.size(); i++) {
                DTOBooks dto = this.transformEntityToDtoSB.transformBookToDTOBooks(lstBooks.get(i));
                lstDTO.add(dto);
            }
            
            return lstDTO;
            
        }
        catch(Exception e){
            throw new Exception("getBooksByAuthor", e);
        }
    }
    
    @Override
    public List<DTOBooks> getBooksByTitle(String title) throws Exception{
        try{
            List<Books> lstBooks = persistenceSB.getBooksByTitle(title);
            List<DTOBooks> lstDTO = new ArrayList<DTOBooks>();
            
            for (int i = 0; i < lstBooks.size(); i++) {
                DTOBooks dto = this.transformEntityToDtoSB.transformBookToDTOBooks(lstBooks.get(i));
                lstDTO.add(dto);
            }
            
            return lstDTO;
        }
        catch(Exception e){
            throw new Exception("getBooksByTitle",e);
        }
    }
    
    @Override
    public DTOBooks getBook(String isbn) throws Exception{
        try{
            Books book = persistenceSB.getBook(isbn);
            if(book != null){
                return this.transformEntityToDtoSB.transformBookToDTOBooks(book);
            }
            else{
                return null;
            }
        }
        catch(Exception e){
            throw new Exception("getBook",e);
        }
    }
    
    @Override
    public boolean existsBook(String isbn) throws Exception{
        try{
           return persistenceSB.getBook(isbn) != null; 
        }
        catch(Exception e){
            throw new Exception("The ISBN book " + isbn + "doesn't exists");
        }
    }
    
    @Override
    public List<DTOBooks> getAllBooks() throws Exception{
        try{
            List<Books> lstBooks = persistenceSB.getAllBooks();
            List<DTOBooks> lstDto = new ArrayList<DTOBooks>();
            for (int i = 0; i < lstBooks.size(); i++) {
                DTOBooks dto = this.transformEntityToDtoSB.transformBookToDTOBooks(lstBooks.get(i));
                lstDto.add(dto);
                
            }
            return lstDto;
        }
        catch(Exception e){
            return null;
        }
    }
}
