package info.stasha.testosterone.jersey.junit4.integration.app.user;

import info.stasha.testosterone.jersey.junit4.integration.app.user.*;
import info.stasha.testosterone.jersey.junit4.integration.app.user.service.UserService;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 *
 * @author stasha
 */
@Path("user")
@Consumes("application/json")
@Produces("application/json")
public class UserResource {

    @Context
    private UserService userService;

    @GET
    public List<User> getAllUsers() throws Exception {
        return userService.getAllUsers();
    }

    @POST
    public void createUser(User user) throws Exception {
        userService.createUser(user);
    }

    @GET
    @Path("{id}")
    public User getUser(@BeanParam User user) throws Exception {
        return userService.getUser(user);
    }

    @PUT
    @Path("{id}")
    public void updateUser(User user) throws Exception {
        userService.updateUser(user);
    }

    @DELETE
    @Path("{id}")
    public void deleteUser(@BeanParam User user) throws Exception {
        userService.deleteUser(user);
    }

}
