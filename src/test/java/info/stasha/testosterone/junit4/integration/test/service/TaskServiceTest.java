package info.stasha.testosterone.junit4.integration.test.service;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.GenericMockitoFactory;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.integration.app.task.Task;
import info.stasha.testosterone.junit4.integration.app.task.dao.TaskDao;
import info.stasha.testosterone.junit4.integration.app.task.dao.TaskDaoImpl;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskService;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskServiceFactory;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class TaskServiceTest implements Testosterone {

    @Context
    private TaskService taskService;

    @Context
    private TaskDao taskDao;

    private final Task task = new Task("New Task", "New Task Description", false).setId(1L);

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(TaskServiceFactory.class).to(TaskService.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
        binder.bindFactory(GenericMockitoFactory.mock(TaskDaoImpl.class)).to(TaskDao.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTest() throws Exception {
        taskService.createTask(new Task(1L));
    }

    @Test(expected = NullPointerException.class)
    public void createTest2() throws Exception {
        taskService.createTask(null);
    }

    @Test
    public void createTest3() throws Exception {
        taskService.createTask(task.setId(null));
        Mockito.verify(taskDao, times(1)).createTask(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void readTest() throws Exception {
        taskService.getTask(new Task());
    }

    @Test(expected = NullPointerException.class)
    public void readTest2() throws Exception {
        taskService.getTask(null);
    }

    @Test
    public void readTest3() throws Exception {
        taskService.getTask(task);
        Mockito.verify(taskDao, times(1)).getTask(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTest() throws Exception {
        taskService.updateTask(new Task());
    }

    @Test(expected = NullPointerException.class)
    public void updateTest2() throws Exception {
        taskService.updateTask(null);
    }

    @Test
    public void updateTest3() throws Exception {
        taskService.updateTask(task);
        Mockito.verify(taskDao, times(1)).updateTask(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTest() throws Exception {
        taskService.deleteTask(new Task());
    }

    @Test(expected = NullPointerException.class)
    public void deleteTest2() throws Exception {
        taskService.deleteTask(null);
    }

    @Test
    public void deleteTest3() throws Exception {
        taskService.deleteTask(task);
        Mockito.verify(taskDao, times(1)).deleteTask(task);
    }

}
