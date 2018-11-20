package info.stasha.testosterone.jersey.junit4.integration.app.task.service;

import info.stasha.testosterone.jersey.junit4.integration.app.task.Task;
import java.util.List;

/**
 *
 * @author stasha
 */
public interface TaskService {

    List<Task> getAllTasks() throws Exception;

    void createTask(Task task) throws Exception;

    Task getTask(Task task) throws Exception;

    void updateTask(Task task) throws Exception;

    void deleteTask(Task task) throws Exception;

}
