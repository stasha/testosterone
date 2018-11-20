package info.stasha.testosterone.jersey.junit4.integration.test.task.resource;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.FactoryUtils;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.junit4.integration.app.task.Task;
import info.stasha.testosterone.jersey.junit4.integration.app.task.TaskResource;
import info.stasha.testosterone.jersey.junit4.integration.app.task.service.TaskService;
import info.stasha.testosterone.jersey.junit4.integration.app.task.service.TaskServiceFactory;
import java.util.List;
import javax.inject.Singleton;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import static javax.ws.rs.HttpMethod.*;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Testing TaskResource by extending it.
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ExtendingTaskResourceTest extends TaskResource implements Testosterone {

    private static boolean allCalled;
    private static boolean createCalled;
    private static boolean getCalled;
    private static boolean updateCalled;
    private static boolean deleteCalled;

    protected final Task task = new Task(3L, "Title", "Description", false);
    protected final Task createTask = new Task("Title", "Description", false);
    public Entity taskEntity = Entity.json(task);
    public Entity createTaskEntity = Entity.json(createTask);

    @Context
    private TaskService taskService;

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bindFactory(FactoryUtils.<TaskService>mock(TaskServiceFactory.class)).to(TaskService.class).in(Singleton.class);
    }

    @Override
    public void afterServerStop() {
        assertTrue("Get all tasks was called", allCalled);
        assertTrue("Create task was called", createCalled);
        assertTrue("Get task was called", getCalled);
        assertTrue("Update task was called", updateCalled);
        assertTrue("Delete task was called", deleteCalled);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Override
    public List<Task> getAllTasks() throws Exception {
        allCalled = true;
        return super.getAllTasks();
    }

    @Override
    public void deleteTask(Task task) throws Exception {
        deleteCalled = true;
        super.deleteTask(task);
    }

    @Override
    public void updateTask(Task task) throws Exception {
        updateCalled = true;
        super.updateTask(task);
    }

    @Override
    public Task getTask(Task task) throws Exception {
        getCalled = true;
        return super.getTask(task);
    }

    @Override
    public void createTask(Task task) throws Exception {
        createCalled = true;
        super.createTask(task);
    }

    @Test
    @Request(url = "task", method = GET, expectedStatus = {200, 204})
    public void getAllTasksTest(Response resp) throws Exception {
        verify(taskService, 1).getAllTasks();
    }

    @Test
    @Request(url = "task", method = POST, entity = "taskEntity", expectedStatus = {200, 204})
    public void createTaskTest(Response resp) throws Exception {
        verify(taskService, 1).createTask(Matchers.refEq(task));
    }

    @Test
    @Request(url = "task/1", method = GET, expectedStatus = {200, 204})
    public void getTaskTest(Response resp) throws Exception {
        verify(taskService, 1).getTask(any(Task.class));
    }

    @Test
    @Request(url = "task/1", method = PUT, entity = "taskEntity", expectedStatus = {200, 204})
    public void updateTaskTest(Response resp) throws Exception {
        verify(taskService, 1).updateTask(Matchers.refEq(task));
    }

    @Test
    @Request(url = "task/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteTaskTest(Response resp) throws Exception {
        verify(taskService, 1).deleteTask(any(Task.class));
    }

}
