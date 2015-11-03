/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.services.rest;

import com.reyk.business.logic.UsersSBLocal;
import com.reyk.dataTransferObjects.DTOUsers;
import javax.ejb.EJB;
import javax.faces.bean.RequestScoped;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * REST Web Service
 *
 * @author MacAA
 */
@Path("generic")
@RequestScoped
public class ServicesWS {

    @Context
    private UriInfo context;
    
    @EJB
    UsersSBLocal usersSB;
    
   

    /**
     * Creates a new instance of ServicesWS
     */
    public ServicesWS() {
       
    }
    
    /**
     * Retrieves representation of an instance of com.reyk.services.rest.ServicesWS
     * @return an instance of java.lang.String
     */
    @GET
    @Produces("application/xml")
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of ServicesWS
     * @param content representation for the resource
     * @return an HTTP response with content of the updated or created resource.
     */
    @PUT
    @Consumes("application/xml")
    public void putXml(String content) {
    }
    
    @POST
    @Path("/addUser")
    @Consumes("application/json")
    public Response addUser(String json){

        Gson gson = new Gson();
        
        DTOUsers dto = gson.fromJson(json, DTOUsers.class);
        
        usersSB.addUser(dto);
        return Response.accepted("Success").build();
    }
    
    @POST
    @Path("/addMultipleUsers")
    @Consumes("application/json")
    public Response addMultipleUsers(String json){
        try{
        Gson gson = new Gson();
        List<DTOUsers> lst = new ArrayList<DTOUsers>();
        lst = Arrays.asList(gson.fromJson(json, DTOUsers[].class));
        
        for (int i = 0; i < lst.size(); i++) 
        {
             usersSB.addUser(lst.get(i));
        }
        
        return Response.accepted("OKKKK").build();
        }
        catch(Exception e){
            return Response.accepted(e.getMessage()).build();
        }
        }
    
    
    @POST
    @Path("/addAuthor")
    @Consumes("application/x-www-form-urlencoded")
    public String addAuthor(
            @FormParam("name") String name,
            @FormParam("username") String username, 
            @FormParam("password") String password){
     
        DTOUsers u = new DTOUsers(name, "apellido", username, password, "email del pibe");
        usersSB.addUser(u);
        
        return "User: " + name + " has been added";
    }
    
    @GET
    @Path("/getUsers")
    @Consumes("application/json")
    public Response getUsers(){
        
        if(usersSB != null){
            if(usersSB.getUsers().size() == 0){
                return Response.ok("There are no users in the system").build();
            
            }
            else{
                 Gson gson = new Gson(); // Or use new GsonBuilder().create();
                 List<DTOUsers> target = usersSB.getUsers();
                return Response.accepted(gson.toJson(target)).build();
           
            }
        } else {
            
            return Response.ok("El SB es null").build();
        }
       
        
        
       
    }
    
   
    
}
