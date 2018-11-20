package info.stasha.testosterone.jersey.junit4.integration.app.task.service;


import info.stasha.testosterone.jersey.junit4.integration.app.task.Task;
import info.stasha.testosterone.jersey.junit4.integration.app.task.dao.TaskDao;

import java.util.List;
import javax.ws.rs.core.Context;

/**
 *
 * @author stasha
 */
public class TaskServiceImpl implements TaskService {

    @Context
    private TaskDao taskDao;

    @Override
    public List<Task> getAllTasks() throws Exception {
        return taskDao.getAllTasks();
    }

    @Override
    public void createTask(Task task) throws Exception {
        if (task.getId() != null) {
            throw new IllegalArgumentException("You can't create task with existing ID");
        }
        taskDao.createTask(task);
    }

    @Override
    public Task getTask(Task task) throws Exception {
        if (task.getId() == null) {
            throw new IllegalArgumentException("taskid must not be null");
        }
        return taskDao.getTask(task);
    }

    @Override
    public void updateTask(Task task) throws Exception {
        if (task.getId() == null) {
            throw new IllegalArgumentException("User with null id can't be updated");
        }

        taskDao.updateTask(task);
    }

    @Override
    public void deleteTask(Task task) throws Exception {
        if (task.getId() == null) {
            throw new IllegalArgumentException("User with null id can't be deleted");
        }

        taskDao.deleteTask(task);
    }

}
