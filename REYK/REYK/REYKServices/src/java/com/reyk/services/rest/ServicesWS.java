/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.reyk.services.rest;

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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.dataTransferObjects.DTOMessages;
import com.reyk.dataTransferObjects.DTOUsers;
import com.reyk.business.logic.BookingsSBLocal;
import com.reyk.business.logic.BooksSBLocal;
import com.reyk.business.logic.MessagesSBLocal;
import com.reyk.business.logic.UsersSBLocal;
import com.reyk.business.jms.Publisher;
import com.reyk.business.jms.Subscriber;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.EJBException;
import javax.jms.Session;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicSession;
import javax.json.JsonException;
import javax.naming.InitialContext;

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

    @EJB
    BooksSBLocal booksSB;

    @EJB
    BookingsSBLocal bookingsSB;

    @EJB
    MessagesSBLocal messagesSB;

    /**
     * Creates a new instance of ServicesWS
     */
    public ServicesWS() {

    }

    /**
     * Retrieves representation of an instance of
     * com.reyk.services.rest.ServicesWS
     *
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
     *
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
    public Response addUser(String json) throws Exception {
        try {
            Gson gson = new Gson();
            DTOUsers dto = gson.fromJson(json, DTOUsers.class);
            if (dto.getUsername() != null && dto.getPassword() != null) {
                if (dto != null && !dto.getUsername().isEmpty() && !dto.getPassword().isEmpty()) {
                    if (usersSB.exists(dto.getUsername()) == false) {
                        usersSB.addUser(dto);
                        return Response.accepted("Success").build();
                    }
                    return Response.accepted("Username already exists.").build();
                } else {
                    return Response.accepted("Can't add user.").build();
                }
            } else {
                return Response.accepted("Username and password can not be empty").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/addMultipleUsers")
    @Consumes("application/json")
    public Response addMultipleUsers(String json) {
        try {
            Gson gson = new Gson();
            List<DTOUsers> lst = new ArrayList<DTOUsers>();
            lst = Arrays.asList(gson.fromJson(json, DTOUsers[].class));
            int count = 0;
            for (int i = 0; i < lst.size(); i++) {
                if (lst.get(i).getUsername() != null && lst.get(i).getPassword() != null) {
                    if (!usersSB.exists((lst.get(i).getUsername()))) {
                        usersSB.addUser(lst.get(i));
                        count++;
                    }
                }
            }
            return Response.accepted(count + " of " + lst.size() + " were added").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getUsers")
    @Consumes("application/json")
    public Response getUsers() {
        try {
            if (usersSB != null) {
                if (usersSB.getUsers().size() == 0) {
                    return Response.ok("There are no users in the system").build();
                } else {
                    Gson gson = new Gson(); // Or use new GsonBuilder().create();
                    List<DTOUsers> target = usersSB.getUsers();
                    return Response.accepted(gson.toJson(target)).build();
                }
            } else {
                return Response.ok("El SB es null").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/deleteUser")
    @Consumes("application/json")
    public Response deleteUser(String username) throws Exception {
        try {
            Map<String, String> map = new Gson().fromJson(username, new TypeToken<Map<String, String>>() {
            }.getType());
            String usernameA = map.get("username");
            if (usersSB.exists(usernameA)) {
                int count = usersSB.getUsers().size();
                usersSB.deleteUser(usernameA);
                int count2 = usersSB.getUsers().size();
                if (count > count2) {
                    return Response.accepted("Successfully deleted " + usernameA).build();
                } else {
                    return Response.accepted("Error: can't delete " + username).build();
                }
            }
            return Response.accepted("Error: can't delete " + username + " the user doesn't exists").build();
        } catch (Exception e) {
            return Response.accepted("Unknown erro occured. Please try again.").build();
        }
    }

    @POST
    @Path("/existUser")
    @Consumes("application/json")
    public Response existsUser(String username) throws Exception {
        try {
            Map<String, String> map = new Gson().fromJson(username, new TypeToken<Map<String, String>>() {
            }.getType());
            String usernameA = map.get("username");
            if (usernameA != null && !usernameA.isEmpty()) {
                if (usersSB.exists(usernameA)) {
                    return Response.accepted("User: " + usernameA + " exists").build();
                } else {
                    return Response.accepted("User: " + usernameA + " does not exists").build();
                }
            } else {
                return Response.accepted("Username can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted("Unknown error occured").build();
        }
    }

    @POST
    @Path("/updateUser")
    @Consumes("application/json")
    public Response updateUser(String json) throws Exception {
        try {
            Gson gson = new Gson();
            DTOUsers dto = gson.fromJson(json, DTOUsers.class);
            if (dto.getUsername() != null && !dto.getUsername().isEmpty()) {
                if (usersSB.exists(dto.getUsername())) {
                    usersSB.updateUser(dto);
                    return Response.accepted("The user " + dto.getUsername() + " was successfully updated").build();
                } else {
                    return Response.accepted("The user " + dto.getUsername() + " doesn't exists").build();
                }
            } else {
                return Response.accepted("The username can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted("Unknown error occured. Please try again.").build();
        }
    }

    @POST
    @Path("/getUser")
    @Consumes("application/json")
    public Response getUser(String username) {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(username, new TypeToken<Map<String, String>>() {
            }.getType());
            String usernameA = map.get("username");
            if (usernameA != null && !username.isEmpty()) {
                DTOUsers aux = usersSB.getUser(usernameA);
                return Response.accepted(gson.toJson(aux)).build();
            } else {
                return Response.accepted("The username can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Login ">
    @POST
    @Path("/login")
    @Consumes("application/json")
    public Response login(String login) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> mapUser = gson.fromJson(login, new TypeToken<Map<String, String>>() {
            }.getType());
            String usernameA = mapUser.get("username");
            String passwordA = mapUser.get("password");
            if (usernameA != null && !usernameA.isEmpty()) {
                if (passwordA != null && !passwordA.isEmpty()) {
                    String token = usersSB.authenticatorToken(usernameA, passwordA);
                    String result = usersSB.login(token, usernameA, passwordA);
                    if (result != "") {
                        return Response.accepted(result).build();  //Retorna el token de seguridad
                    } else {
                        return Response.accepted("Username and password doesn't match: " + usernameA + " = " + passwordA).build();
                    }
                }
                return Response.accepted("The password can not be null or empty").build();
            }
            return Response.accepted("The username can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/logout")
    @Consumes("application/json")
    public Response logout(String token) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> mapToken = gson.fromJson(token, new TypeToken<Map<String, String>>() {
            }.getType());
            String _token = mapToken.get("token");
            if (_token != null && !_token.isEmpty()) {
                String result = usersSB.logout(_token);
                return Response.accepted(result).build();
            }
            return Response.accepted("The token can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Social Media ">
    @POST
    @Path("/connectToSocialMedia")
    @Consumes("application/json")
    public Response connectToSocialMedia(String json) {
        try {
            Gson gson = new Gson();
            Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = mapUser.get("username");
            String socialMedia = mapUser.get("socialMedia");
            String securityToken = mapUser.get("token"); //Given when Logged into REYK
            if (username != null && !username.isEmpty()) {
                if (socialMedia != null && !socialMedia.isEmpty()) {
                    if (securityToken != null && !securityToken.isEmpty()) {
                        if (usersSB.isLoggedIn(username, securityToken)) {
                            String authorizationUrl = usersSB.connectToSocialMedia(socialMedia, username);
                            return Response.accepted(authorizationUrl).build();
                        } else {
                            return Response.accepted("You must login.").build();
                        }
                    }
                    return Response.accepted("The security token can not be null or empty").build();
                }
                return Response.accepted("The social media can not be null or empty").build();
            }
            return Response.accepted("The username can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/addPin")
    @Consumes("application/json")
    public Response addPin(String json) {
        try {
            Gson gson = new Gson();
            Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = mapUser.get("username");
            String pin = mapUser.get("pin");
            String securityToken = mapUser.get("token"); //Given when Logged into REYK
            if (username != null && !username.isEmpty()) {
                if (pin != null && !pin.isEmpty()) {
                    if (securityToken != null && !securityToken.isEmpty()) {
                        if (usersSB.isLoggedIn(username, securityToken)) {
                            usersSB.addPin(pin, username);
                            return Response.accepted("Correct pin").build();
                        } else {
                            return Response.accepted("You must login.").build();
                        }
                    }
                    return Response.accepted("The security token can not be null or empty").build();
                }
                return Response.accepted("The pin can not be null or empty").build();
            }
            return Response.accepted("The username can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/postComment")
    @Consumes("application/json")
    public Response postComment(String json) {
        try {
            Gson gson = new Gson();
            Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = mapUser.get("username");
            String _socialMedia = mapUser.get("socialMedia");
            String securityToken = mapUser.get("token"); //Given when Logged into REYK
            String comment = mapUser.get("comment");
            if (username != null && !username.isEmpty()) {
                if (_socialMedia != null && !_socialMedia.isEmpty()) {
                    if (securityToken != null && !securityToken.isEmpty()) {
                        if (comment != null && !comment.isEmpty()) {
                            if (usersSB.isLoggedIn(username, securityToken)) {
                                usersSB.postComment(comment, username, _socialMedia);
                                return Response.accepted("Post comment successfully").build();
                            } else {
                                return Response.accepted("You must login.").build();
                            }
                        }
                        return Response.accepted("The comment can not be null or empty").build();
                    }
                    return Response.accepted("The security token can not be null or empty").build();
                }
                return Response.accepted("The social media can not be null or empty").build();
            }
            return Response.accepted("The username can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted("Unknown error. Try again.").build();
        }
    }

    @POST
    @Path("/disconnectFromSocialMedia")
    @Consumes("application/json")
    public Response disconnectFromSocialMedia(String json) {
        try {
            Gson gson = new Gson();
            Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = mapUser.get("username");
            String _socialMedia = mapUser.get("socialMedia");
            String securityToken = mapUser.get("token"); //Given when Logged into REYK
            if (username != null && !username.isEmpty()) {
                if (_socialMedia != null && !_socialMedia.isEmpty()) {
                    if (securityToken != null && !securityToken.isEmpty()) {
                        if (usersSB.isLoggedIn(username, securityToken)) {
                            usersSB.disconnectFromSocialMedia(_socialMedia, username);
                            return Response.accepted("Disconnected successfully").build();
                        } else {
                            return Response.accepted("You must login.").build();
                        }
                    }
                    return Response.accepted("The security token can not be null or empty").build();
                }
                return Response.accepted("The social media can not be null or empty").build();
            }
            return Response.accepted("The username can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Books ">
    @POST
    @Path("/addBook")
    @Consumes("application/json")
    public Response addBook(String json) throws Exception {
        try {
            Gson gson = new Gson();
            DTOBooks dto = gson.fromJson(json, DTOBooks.class);
            if (dto != null && dto.getIsbn() != null && !dto.getIsbn().isEmpty()) {
                if (dto.getAuthor() != null && dto.getGenre() != null && !dto.getAuthor().isEmpty() && !dto.getAuthor().isEmpty()) {
                    if (!booksSB.existsBook(dto.getIsbn())) {
                        booksSB.addBook(dto);
                        List<DTOUsers> listUser = usersSB.getUsers();
                        InitialContext ic = new InitialContext();
                        TopicConnectionFactory connectionFactory = (TopicConnectionFactory) ic.lookup("ConnectionFactory");
                        TopicConnection connection = connectionFactory.createTopicConnection();
                        javax.jms.Topic topic = (javax.jms.Topic) ic.lookup("jms/Topic");
                        TopicSession session = connection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
                        Publisher publisher = new Publisher(topic, session, dto.getTitle());
                        for (int i = 0; i < listUser.size(); i++) {
                            if (listUser.get(i).isSuscribed()) {
                                Subscriber subscriber = new Subscriber(topic, session, listUser.get(i).getName(), dto.getTitle());
                                String msj = "El nuevo libro publicado es: " + dto.getTitle();
                                DTOMessages mes = new DTOMessages(listUser.get(i), false, msj);
                                messagesSB.addMessages(mes);
                            }
                        }
                        connection.start();
                        Thread thread = new Thread(publisher);
                        thread.start();
                        thread.join();
                        publisher.close();
                        connection.close();
                        ic.close();
                        return Response.accepted("The book: " + dto.getTitle() + " has been added.").build();
                    } else {
                        return Response.accepted("Book with the isbn " + dto.getIsbn() + " already exists.").build();
                    }
                } else {
                    return Response.accepted("The Author and Genre can not be null or empty").build();
                }
            } else {
                return Response.accepted("The book must have and ISBN.").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/getBooksByAuthor")
    @Consumes("application/json")
    public Response getBookByAuthor(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String author = map.get("author");
            if (author != null && !author.isEmpty()) {
                List<DTOBooks> lstBooks = booksSB.getBooksByAuthor(author);
                if (lstBooks.size() == 0) {
                    return Response.accepted("There are no books written by " + author).build();
                }
                return Response.accepted(gson.toJson(lstBooks)).build();
            } else {
                return Response.accepted("The author can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/getBooksByGenre")
    @Consumes("application/json")
    public Response getBookByGenre(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String genre = map.get("genre");
            if (genre != null && !genre.isEmpty()) {
                List<DTOBooks> lstBooks = booksSB.getBooksByGenre(genre);
                if (lstBooks.size() == 0) {
                    return Response.accepted("There are no books with the genre " + genre).build();
                }
                return Response.accepted(gson.toJson(lstBooks)).build();
            } else {
                return Response.accepted("The genre can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @POST
    @Path("/getBook")
    @Consumes("application/json")
    public Response getBook(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String isbn = map.get("isbn");
            if (isbn != null && !isbn.isEmpty()) {
                DTOBooks book = booksSB.getBook(isbn);
                return Response.accepted(gson.toJson(book)).build();
            }
            return Response.accepted("The isbn can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getAllBooks")
    @Consumes("application/json")
    public Response getAllBooks() throws Exception {
        try {
            Gson gson = new Gson();
            List<DTOBooks> book = booksSB.getAllBooks();
            if (book.size() != 0) {
                return Response.accepted(gson.toJson(book)).build();
            }
            return Response.accepted("There are no books").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Bookings  ">
    @POST
    @Path("/addBooking")
    @Consumes("application/json")
    public Response addBooking(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String isbn = map.get("isbn");
            String username = map.get("username");
            String token = map.get("token");
            if (isbn != null && !isbn.isEmpty()) {
                if (username != null && !username.isEmpty()) {
                    if (token != null && !token.isEmpty()) {
                        DTOBooks book = booksSB.getBook(isbn);
                        if (book == null) {
                            return Response.accepted("The book with ISBN " + isbn + " doesn't exists.").build();
                        }
                        DTOUsers user = usersSB.getUser(username);
                        if (user == null) {
                            return Response.accepted("The user " + username + " doesn't exists").build();
                        }
                        Date dateInitial = new Date();
                        int noOfDays = 14;
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(dateInitial);
                        calendar.add(Calendar.DAY_OF_YEAR, noOfDays);
                        Date dateFinal = calendar.getTime();
                        if (usersSB.isLoggedIn(username, token)) {
                            DTOBookings dtoBooking = new DTOBookings(user, book, dateInitial, dateFinal);
                            bookingsSB.addBooking(dtoBooking);
                            return Response.accepted("The book " + book.getTitle() + " is booked by " + user.getUsername()
                                    + " from " + dtoBooking.getInitialDate() + " to " + dtoBooking.getFinalDate()).build();
                        } else {
                            return Response.accepted("The user must be logged in").build();
                        }
                    } else {
                        return Response.accepted("The token can not be null or empty").build();
                    }
                } else {
                    return Response.accepted("The username can not be null or empty").build();
                }
            } else {
                return Response.accepted("The isbn can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }

    }

    @POST
    @Path("/getBookingsByUser")
    @Consumes("application/json")
    public Response getBookingsByUser(String json) {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = map.get("username");
            if (username != null && !username.isEmpty()) {
                DTOUsers user = usersSB.getUser(username);
                if (user != null) {
                    List<DTOBookings> bookings = bookingsSB.getBookingsByUser(user);
                    if (bookings.size() == 0) {
                        return Response.accepted("The are no bookings").build();
                    }
                    return Response.accepted(gson.toJson(bookings)).build();
                } else {
                    return Response.accepted("The user " + username + " doesn't exists").build();
                }
            }
            return Response.accepted("The username can not be null or empty").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

//</editor-fold>    
    // <editor-fold defaultstate="collapsed" desc=" Messages ">
    @POST
    @Path("/getMessages")
    @Consumes("application/json")
    public Response getMessages(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = map.get("username");
            if (username != null && !username.isEmpty()) {
                if (usersSB.exists(username)) {
                    List<DTOMessages> message = messagesSB.getMessages(username);
                    if (message.size() == 0) {
                        return Response.accepted("There are not new messages").build();
                    }
                    for (int i = 0; i < message.size(); i++) {
                        messagesSB.messageSeen(message.get(i));
                        message.get(i).setId(null);
                    }
                    return Response.accepted(gson.toJson(message)).build();
                } else {
                    return Response.accepted("The username " + username + " could not be found").build();
                }
            } else {
                return Response.accepted("The username can not be null or empty").build();
            }
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }
    //</editor-fold>
}
