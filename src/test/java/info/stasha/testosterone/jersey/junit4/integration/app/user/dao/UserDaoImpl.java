package info.stasha.testosterone.jersey.junit4.integration.app.user.dao;

import info.stasha.testosterone.jersey.junit4.integration.app.user.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Context;

/**
 *
 * @author stasha
 */
public class UserDaoImpl implements UserDao {

    @Context
    Connection conn;

    private User getUser(ResultSet rs) throws SQLException {
        User t = new User();
        t.setId(rs.getLong(1));
        t.setFirstName(rs.getString(2));
        t.setLastName(rs.getString(3));
        t.setAge(rs.getInt(4));
        return t;
    }

    @Override
    public List<User> getAllUsers() throws SQLException {
        List<User> allUsers = new ArrayList<>();
        try (ResultSet rs = conn.prepareStatement("select * from users").executeQuery()) {
            while (rs.next()) {
                allUsers.add(getUser(rs));
            }
        }
        return allUsers;
    }

    @Override
    public void createUser(User user) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("insert into users (firstName, lastName, age) values (?, ?, ?)")) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();
        }
    }

    @Override
    public User getUser(User user) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("select * from users where id = ?")) {
            ps.setLong(1, user.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getUser(rs);
                }

                return null;
            }
        }
    }

    @Override
    public void updateUser(User user) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("update users set firstName = ?, lastName = ?, age = ? where id = ?")) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.setLong(4, user.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteUser(User user) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("delete from users where id = ?")) {
            ps.setLong(1, user.getId());
            ps.executeUpdate();
        }
    }

}
