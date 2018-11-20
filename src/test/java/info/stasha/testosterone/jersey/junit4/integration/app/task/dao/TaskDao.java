package info.stasha.testosterone.jersey.junit4.integration.app.task.dao;

import info.stasha.testosterone.jersey.junit4.integration.app.task.Task;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author stasha
 */
public interface TaskDao {

    List<Task> getAllTasks() throws SQLException;

    void createTask(Task task) throws SQLException;

    Task getTask(Task task) throws SQLException;

    void updateTask(Task task) throws SQLException;

    void deleteTask(Task task) throws SQLException;
}
