package info.stasha.testosterone.junit4.integration.app.user.dao;

import info.stasha.testosterone.junit4.integration.app.user.User;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author stasha
 */
public interface UserDao {

    List<User> getAllUsers() throws SQLException;

    void createUser(User user) throws SQLException;

    User getUser(User user) throws SQLException;

    void updateUser(User user) throws SQLException;

    void deleteUser(User user) throws SQLException;
}
