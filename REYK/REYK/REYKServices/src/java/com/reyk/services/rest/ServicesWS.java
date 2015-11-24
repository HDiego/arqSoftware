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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.reyk.business.logic.BookingsSBLocal;
import com.reyk.business.logic.BooksSBLocal;
import com.reyk.dataTransferObjects.DTOBookings;
import com.reyk.business.logic.MessagesSBLocal;
import com.reyk.dataTransferObjects.DTOBooks;
import com.reyk.dataTransferObjects.DTOMessages;
import com.reyk.services.jms.Publisher;
import com.reyk.services.jms.Subscriber;
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

        Gson gson = new Gson();
        DTOUsers dto = gson.fromJson(json, DTOUsers.class);
        if (dto != null && !dto.getUsername().isEmpty()) {

            if (usersSB.exists(dto.getUsername()) == false) {

                try {
                    usersSB.addUser(dto);
                    return Response.accepted("Success").build();
                } catch (Exception e) {
                    throw new Exception("Tiro exception", e);
                }
            }
            return Response.accepted("Username already exists.").build();
        } else {
            return Response.accepted("Can't add user.").build();
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

            for (int i = 0; i < lst.size(); i++) {
                usersSB.addUser(lst.get(i));
            }

            return Response.accepted("OKKKK").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }

    @GET
    @Path("/getUsers")
    @Consumes("application/json")
    public Response getUsers() {

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

            if (usersSB.exists(usernameA)) {
                return Response.accepted("User: " + usernameA + " exists").build();
            } else {
                return Response.accepted("User: " + usernameA + " does not exists").build();
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
            if (usersSB.exists(dto.getUsername())) {
                usersSB.updateUser(dto);
                return Response.accepted("The user " + dto.getUsername() + " was successfully updated").build();
            } else {
                return Response.accepted("The user " + dto.getUsername() + " doesn't exists").build();
            }
        } catch (Exception e) {
            return Response.accepted("Unknown error occured. Please try again.").build();
        }
    }

    @POST
    @Path("/getUser")
    @Consumes("application/json")
    public Response getUser(String username) {
        Gson gson = new Gson();
        Map<String, String> map = gson.fromJson(username, new TypeToken<Map<String, String>>() {
        }.getType());
        String usernameA = map.get("username");
        DTOUsers aux = usersSB.getUser(usernameA);
        return Response.accepted(gson.toJson(aux)).build();
    }

    // </editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Login ">
    @POST
    @Path("/login")
    @Consumes("application/json")
    public Response login(String login) throws Exception {
        Gson gson = new Gson();

        Map<String, String> mapUser = gson.fromJson(login, new TypeToken<Map<String, String>>() {
        }.getType());

        String usernameA = mapUser.get("username");
        String passwordA = mapUser.get("password");
        String token = usersSB.authenticatorToken(usernameA);

        String result = usersSB.login(token, usernameA, passwordA);

        if (result != "") {
            return Response.accepted(result).build();  //Retorna el token de seguridad
        } else {
            return Response.accepted("Username and password doesn't match: " + usernameA + " = " + passwordA).build();
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
            String result = usersSB.logout(_token);
            return Response.accepted(result).build();
        } catch (Exception e) {
            throw new Exception("Error", e);
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Social Media ">
    @POST
    @Path("/connectToSocialMedia")
    @Consumes("application/json")
    public Response connectToSocialMedia(String json) {
        Gson gson = new Gson();
        Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        String username = mapUser.get("username");
        String socialMedia = mapUser.get("socialMedia");
        String securityToken = mapUser.get("token"); //Given when Logged into REYK
        if (usersSB.isLoggedIn(username, securityToken)) {
            try {
                String authorizationUrl = usersSB.connectToSocialMedia(socialMedia, username);
                return Response.accepted(authorizationUrl).build();

            } catch (Exception ex) {
                return Response.accepted(ex.getMessage()).build();
            }

        } else {
            return Response.accepted("You must login.").build();
        }
    }

    @POST
    @Path("/addPin")
    @Consumes("application/json")
    public Response addPin(String json) {
        Gson gson = new Gson();
        Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        String username = mapUser.get("username");
        String pin = mapUser.get("pin");
        String securityToken = mapUser.get("token"); //Given when Logged into REYK

        if (usersSB.isLoggedIn(username, securityToken)) {
            try {
                usersSB.addPin(pin, username);
                return Response.accepted("Correct pin").build();
            } catch (Exception e) {
                return Response.accepted(e.getMessage()).build();
            }
        } else {
            return Response.accepted("You must login.").build();
        }
    }

    @POST
    @Path("/postComment")
    @Consumes("application/json")
    public Response postComment(String json) {
        Gson gson = new Gson();
        Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        String username = mapUser.get("username");
        String _socialMedia = mapUser.get("socialMedia");
        String securityToken = mapUser.get("token"); //Given when Logged into REYK
        String comment = mapUser.get("comment");

        if (usersSB.isLoggedIn(username, securityToken)) {
            try {
                usersSB.postComment(comment, username, _socialMedia);
                return Response.accepted("Post comment successfully").build();
            } catch (EJBException e) {
                return Response.accepted(e.getMessage()).build();
            } catch (Exception e) {
                return Response.accepted("Unknown error. Try again.").build();
            }
        } else {
            return Response.accepted("You must login.").build();
        }
    }

    @POST
    @Path("/disconnectFromSocialMedia")
    @Consumes("application/json")
    public Response disconnectFromSocialMedia(String json) {

        Gson gson = new Gson();
        Map<String, String> mapUser = gson.fromJson(json, new TypeToken<Map<String, String>>() {
        }.getType());

        String username = mapUser.get("username");
        String _socialMedia = mapUser.get("socialMedia");
        String securityToken = mapUser.get("token"); //Given when Logged into REYK

        if (usersSB.isLoggedIn(username, securityToken)) {
            try {
                usersSB.disconnectFromSocialMedia(_socialMedia, username);
                return Response.accepted("Disconnected successfully").build();
            } catch (EJBException e) {
                return Response.accepted(e.getMessage()).build();
            } catch (Exception e) {
                return Response.accepted("Error. Try again.").build();
            }
        } else {
            return Response.accepted("You must login.").build();
        }
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc=" Books ">
    @POST
    @Path("/addBook")
    @Consumes("application/json")
    public Response addBook(String json) throws Exception {

        Gson gson = new Gson();
        DTOBooks dto = gson.fromJson(json, DTOBooks.class);

        if (dto != null && !dto.getIsbn().isEmpty()) {
            if (!booksSB.existsBook(dto.getIsbn())) {
                try {
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
                } catch (Exception e) {
                    throw new Exception("Tiro exception", e);
                }
            }
            return Response.accepted("Book already exists.").build();
        } else {
            return Response.accepted("The book must have and ISBN.").build();
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
            List<DTOBooks> lstBooks = booksSB.getBooksByAuthor(author);

            return Response.accepted(gson.toJson(lstBooks)).build();

        } catch (Exception e) {
            return Response.accepted("Error getBookByAuthor en Service WS").build();
        }

    }

    @POST
    @Path("/getBooksByTitle")
    @Consumes("application/json")
    public Response getBookByTitle(String json) throws Exception {
        try {

            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String title = map.get("title");
            if (title != "") {
                List<DTOBooks> lstBooks = booksSB.getBooksByTitle(title);

                return Response.accepted(gson.toJson(lstBooks)).build();
            } else {
                return Response.accepted("You must enter a title").build();
            }

        } catch (JsonException je) {
            return Response.accepted("Invalid Json format").build();
        } catch (Exception e) {
            return Response.accepted("Error getBookByTitle en Service WS").build();
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
            DTOBooks book = booksSB.getBook(isbn);
            if (book != null) {
                return Response.accepted(gson.toJson(book)).build();
            }

            return Response.accepted("There are no books with ISBN " + isbn).build();

        } catch (Exception e) {
            return Response.accepted("Error getBook en Service WS").build();
        }

    }

    @GET
    @Path("/getAllBooks")
    @Consumes("application/json")
    public Response getAllBooks() throws Exception {
        try {
            Gson gson = new Gson();

            List<DTOBooks> book = booksSB.getAllBooks();
            if (book != null) {
                return Response.accepted(gson.toJson(book)).build();
            } else {
                return Response.accepted("There are no books").build();
            }

        } catch (Exception e) {
            return Response.accepted("Error from getAllBooks WS").build();
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

            DTOBookings dtoBooking = new DTOBookings(user, book, dateInitial, dateFinal);
            bookingsSB.addBooking(dtoBooking);
            return Response.accepted("The book " + book.getTitle() + " is booked by " + user.getUsername()
                    + "from " + dtoBooking.getInitialDate() + " to " + dtoBooking.getFinalDate()).build();
        } catch (JsonException e) {
            return Response.accepted("Malformed json").build();
        }

    }

    @POST
    @Path("/modifyBooking")
    @Consumes("application/json")
    public Response modifyBooking(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());

            Map<String, Date> map2 = gson.fromJson(json, new TypeToken<Map<String, Date>>() {
            }.getType());

            Date initDate = map2.get("dateInit");
            Date finalDate = map2.get("dateFinal");

            String isbn = map.get("isbn");
            String username = map.get("username");

            DTOBooks book = booksSB.getBook(isbn);
            if (book == null) {
                return Response.accepted("The book with ISBN " + isbn + " doesn't exists.").build();
            }

            DTOUsers user = usersSB.getUser(username);
            if (user == null) {
                return Response.accepted("The user " + username + " doesn't exists").build();
            }
            DTOBookings dtoBooking = new DTOBookings(user, book, initDate, finalDate);
            bookingsSB.modifyBooking(dtoBooking);

            return Response.accepted("Booking successfully modified").build();

        } catch (JsonException jE) {
            return Response.accepted("Malformed json").build();
        } catch (Exception e) {
            return Response.accepted("Please try again").build();
        }
    }

    @POST
    @Path("/getBookingsByUser")
    @Consumes("application/json")
    public Response getBookingsByUser(String json
    ) {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());

            String username = map.get("username");

            DTOUsers user = usersSB.getUser(username);
            if (user != null) {
                List<DTOBookings> bookings = bookingsSB.getBookingsByUser(user);

                return Response.accepted(gson.toJson(bookings)).build();

            } else {
                return Response.accepted("The user " + username + " doesn't exists").build();
            }

        } catch (JsonSyntaxException jEx) {
            return Response.accepted("Malformed json.").build();
        } catch (Exception e) {
            return Response.accepted(e.getMessage()).build();
        }
    }
    
//</editor-fold>    
    
// <editor-fold defaultstate="collapsed" desc="Messages">

    @POST
    @Path("/getMessages")
    @Consumes("application/json")

    public Response getMessages(String json) throws Exception {
        try {
            Gson gson = new Gson();
            Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String, String>>() {
            }.getType());
            String username = map.get("username");
            List<DTOMessages> message = messagesSB.getMessages(username);
            if (message != null && message.size() > 0) {
                for (int i = 0; i < message.size(); i++) {
                    messagesSB.messageSeen(message.get(i));
                    message.get(i).setId(null);
                }
                return Response.accepted(gson.toJson(message)).build();
            } else {
                return Response.accepted("There are no new messages").build();
            }
        } catch (Exception e) {
            return Response.accepted("Error from getMessages WS").build();
        }
    }

//</editor-fold>
}
