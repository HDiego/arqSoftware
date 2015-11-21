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
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.ws.rs.DELETE;
import javax.ws.rs.PathParam;
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
    
    // <editor-fold defaultstate="collapsed" desc=" Users ">
    
    @POST
    @Path("/addUser")
    @Consumes("application/json")
    public Response addUser(String json) throws Exception{

        Gson gson = new Gson();
        DTOUsers dto = gson.fromJson(json, DTOUsers.class);
        if(dto != null && !dto.getUsername().isEmpty()){
            if(!usersSB.exists(dto.getUsername()))
            {
                try{
                    usersSB.addUser(dto);
                    return Response.accepted("Success").build();
                }
                catch(Exception e){
                    throw new Exception("Tiro exception",e);
                }
            }
            return Response.accepted("Username already exists.").build();
        }
        else{
            return Response.accepted("Can't add user.").build();
        }
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
    
    @POST
    @Path("/deleteUser")
    @Consumes("application/json")
    public Response deleteUser(String username) throws Exception
    {
        try{
            Map<String, String> map = new Gson().fromJson(username, new TypeToken<Map<String, String>>() {}.getType());
            String usernameA = map.get("username");
            if(usersSB.exists(usernameA))
            {
                int count = usersSB.getUsers().size();
                usersSB.deleteUser(usernameA);
                int count2 = usersSB.getUsers().size();
                if(count > count2)
                {
                    return Response.accepted("Successfully deleted " + usernameA).build();
                }
                else
                {
                    return Response.accepted("Error: can't delete " + username).build();
                }
            }
            return Response.accepted("Error: can't delete " + username + " the user doesn't exists").build();
        }
        catch(Exception e){
            return Response.accepted("Unknown erro occured. Please try again.").build();
        }
    }
    
    @POST
    @Path("/existUser")
    @Consumes("application/json")
    public Response existsUser(String username) throws Exception{
        try{
            Map<String, String> map = new Gson().fromJson(username, new TypeToken<Map<String, String>>() {}.getType());
            String usernameA = map.get("username");

            if(usersSB.exists(usernameA)){
               return Response.accepted("User: " + usernameA + " exists").build();
            }
            else{
                return Response.accepted("User: " + usernameA + " does not exists").build();
            }
        }
        catch(Exception e){
            return Response.accepted("Unknown error occured").build();
        }
    }
    
    @POST
    @Path("/updateUser")
    @Consumes("application/json")
    public Response updateUser(String json) throws Exception
    {
        try{
            Gson gson = new Gson();
            DTOUsers dto = gson.fromJson(json, DTOUsers.class);
            if(usersSB.exists(dto.getUsername()))
            {
                usersSB.updateUser(dto);
                return Response.accepted("The user " + dto.getUsername() + " was successfully updated").build();
            }
            else
            {
                return Response.accepted("The user " + dto.getUsername() + " doesn't exists").build();
            }
        }
        catch(Exception e)
        {
            return Response.accepted("Unknown error occured. Please try again.").build();
        }
    }
    
    @POST
    @Path("/getUser")
    @Consumes("application/json")
    public Response getUser(String username)
    {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(username, new TypeToken<Map<String, String>>() {}.getType());
        String usernameA = map.get("username");
        DTOUsers aux = usersSB.getUser(usernameA);
        return Response.accepted(gson.toJson(aux)).build();
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc=" Login ">
    @POST
    @Path("/login")
    @Consumes("application/json")
    public Response login(String login) throws Exception{
        Gson gson = new Gson();
     
        Map<String, String> mapUser = gson.fromJson(login, new TypeToken<Map<String, String>>() {}.getType());
      
        String usernameA = mapUser.get("username");
        String passwordA = mapUser.get("password");
        String token = usersSB.authenticatorToken(usernameA);
        
        String result = usersSB.login(token,usernameA, passwordA);
        
        if(result != "")
            return Response.accepted(result).build();  //Retorna el token de seguridad
        else{
            return Response.accepted("Username and password doesn't match: " + usernameA +  " = " + passwordA ).build();
        }
    }
    
    @DELETE
    @Path("/{securityToken}/logout}")
    public Response logout(@PathParam("securityToken") String token) throws Exception{
       // Gson gson = new Gson();
       // Map<String, String> mapToken = gson.fromJson(token, new TypeToken<Map<String, String>>() {}.getType());
        try{
            String result = usersSB.logout(token);
            return Response.accepted(result).build();
        }
        catch(Exception e){
            throw new Exception("Error", e);
        }
        
        
    }
    
    
    
    //</editor-fold>
}
