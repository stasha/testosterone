package info.stasha.testosterone.jersey.junit4.integration.test.user.dao;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import info.stasha.testosterone.jersey.junit4.integration.app.user.dao.UserDao;
import info.stasha.testosterone.jersey.junit4.integration.app.user.dao.UserDaoFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(startServer = StartServer.PER_TEST_METHOD)
public class UserDaoTest implements Testosterone {

    private final String create = "CREATE TABLE users (\n"
            + "  id BIGINT(11) NOT NULL auto_increment PRIMARY KEY,\n"
            + "  firstName VARCHAR(56) NOT NULL,\n"
            + "  lastName VARCHAR(56) NOT NULL,\n"
            + "  age INT NOT NULL,\n"
            + "  created_at DATETIME,\n"
            + "  updated_at DATETIME\n"
            + "  )";

    @Context
    Connection conn;

    @Context
    private UserDao userDao;

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(UserDaoFactory.class).to(UserDao.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Override
    public void configure(DbConfig config) {
        config.add("createUsersTable", create);
    }

    @Override
    public void configureMocks(DbConfig config) {
        config.add("addUsers",
                "insert into users "
                + "(firstName, lastName, age) values "
                + "('Jon', 'doe', 21), "
                + "('Mark', 'Fury', 53), "
                + "('Zippo', 'Martinez', 47)");
    }

    @Before
    public void setUp() throws Exception {
        assertEquals("User list should contain 1 user", 3, userDao.getAllUsers().size());
    }

    @After
    public void tearDown() throws Exception {
        conn.prepareStatement("drop table users").executeUpdate();
    }

    @Test
    public void readAllUsers() throws Exception {
        userDao.createUser(new User("Create User Test 4", "Testing UserDao 2", 33));

        List<User> users = userDao.getAllUsers();
        assertEquals("User list should contain 4 users", 4, users.size());
    }

    @Test
    public void readUser() throws Exception {
        User dbuser = userDao.getUser(new User(3L));
        assertNotNull("Returned user from db should be not null", dbuser);
    }

    @Test
    public void updateUser() throws Exception {
        this.userDao.updateUser(new User(2L, "Update", "User", 54));

        User dbuser = userDao.getUser(new User(2L));
        assertNotNull("Returned user from db should be not null", dbuser);
        assertEquals("Title should be updated", "Update", dbuser.getFirstName());
        assertEquals("Title should be updated", "User", dbuser.getLastName());
        assertEquals("Age should be 54", (Integer) 54, dbuser.getAge());
    }

    @Test
    public void deleteUser() throws Exception {
        this.userDao.deleteUser(new User(1L));

        User dbuser = userDao.getUser(new User(1L));

        assertNull("Returned user should be null", dbuser);
    }

    @Test(expected = NullPointerException.class)
    public void createSqlException() throws SQLException {
        this.userDao.createUser(new User());
    }

}
