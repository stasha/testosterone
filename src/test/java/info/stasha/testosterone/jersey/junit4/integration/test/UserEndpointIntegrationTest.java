package info.stasha.testosterone.jersey.junit4.integration.test;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.junit4.integration.app.user.UserResource;
import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import info.stasha.testosterone.jersey.junit4.integration.test.user.dao.UserDaoTest;
import info.stasha.testosterone.jersey.junit4.integration.test.user.resource.UserResourceTest;
import info.stasha.testosterone.jersey.junit4.integration.test.user.service.UserServiceTest;
import java.sql.SQLException;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@Integration({
    UserResourceTest.class,
    UserServiceTest.class,
    UserDaoTest.class
})
@RunWith(TestosteroneRunner.class)
@Configuration(runDb = true)
public class UserEndpointIntegrationTest implements Testosterone {

    protected UserResource userResource;
    protected final User user = new User(3L, "Title", "Description", 12);
    protected final User createUser = new User("Title", "Description", 22);
    public Entity userEntity = Entity.json(user);
    public Entity createUserEntity = Entity.json(createUser);


    @Before
    public void setUp() throws SQLException {
        target("user").request().post(Entity.json(new User("Jon", "Doe", 23)));
    }

    @Test
    @Request(url = "user", method = GET, expectedStatus = {200, 204})
    public void getAllUsers(Response resp) throws Exception {
    }

    @Test
    @Request(url = "user", method = POST, entity = "userEntity", expectedStatus = {500})
    public void failCreateUser(Response resp) throws Exception {
    }

    @Test
    @Request(url = "user", method = POST, entity = "createUserEntity", expectedStatus = {200, 204})
    public void createUser(Response resp) throws Exception {
    }

    @Test
    @Request(url = "user/1", method = GET, expectedStatus = {200, 204})
    public void getUser(Response resp) throws Exception {
    }

    @Test
    @Request(url = "user/1", method = PUT, entity = "userEntity", expectedStatus = {200, 204})
    public void updateUser(Response resp) throws Exception {
    }

    @Test
    @Request(url = "user/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteUser(Response resp) throws Exception {
    }

}
