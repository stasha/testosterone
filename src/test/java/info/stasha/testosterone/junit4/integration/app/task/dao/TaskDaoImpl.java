package info.stasha.testosterone.junit4.integration.app.task.dao;

import info.stasha.testosterone.junit4.integration.app.task.Task;
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
public class TaskDaoImpl implements TaskDao {

    @Context
    private Connection conn;

    @Override
    public List<Task> getAllTasks() throws SQLException {
        List<Task> allTasks = new ArrayList<>();
        try (ResultSet rs = conn.prepareStatement("select * from tasks").executeQuery()) {
            while (rs.next()) {
                allTasks.add(getTask(rs));
            }
        }
        return allTasks;
    }

    @Override
    public void createTask(Task task) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("insert into tasks (title, description, done) values (?, ?, ?)")) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setBoolean(3, task.getDone());
            ps.executeUpdate();
        }
    }

    @Override
    public Task getTask(Task task) throws SQLException {

        try (PreparedStatement ps = conn.prepareStatement("select * from tasks where id = ?")) {
            ps.setLong(1, task.getId());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return getTask(rs);
                }

                return null;
            }
        }
    }

    @Override
    public void updateTask(Task task) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("update tasks set title = ?, description = ?, done = ? where id = ?")) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setBoolean(3, task.getDone());
            ps.setLong(4, task.getId());
            ps.executeUpdate();
        }
    }

    @Override
    public void deleteTask(Task task) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement("delete from tasks where id = ?")) {
            ps.setLong(1, task.getId());
            ps.executeUpdate();
        }
    }

    private Task getTask(ResultSet rs) throws SQLException {
        Task t = new Task();
        t.setId(rs.getLong(1));
        t.setTitle(rs.getString(2));
        t.setDescription(rs.getString(3));
        t.setDone(rs.getBoolean(4));
        return t;
    }

}
