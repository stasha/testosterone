package info.stasha.testosterone.junit4.integration.test.resource;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Mock;
import info.stasha.testosterone.jersey.MockingAbstractBinder;
import info.stasha.testosterone.jersey.MockingResourceConfig;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.integration.app.task.Task;
import info.stasha.testosterone.junit4.integration.app.task.TaskResource;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskService;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskServiceFactory;
import javax.inject.Singleton;
import static javax.ws.rs.HttpMethod.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;
import org.mockito.Spy;

/**
 * Testing TaskResource with mocks.
 *
 * @author stasha
 */
@Ignore
@RunWith(TestosteroneRunner.class)
public class TaskResourceTest implements Testosterone {

    protected final Task task = new Task(3L, "Title", "Description", false);
    protected final Task createTask = new Task("Title", "Description", false);
    public Entity taskEntity = Entity.json(task);
    public Entity createTaskEntity = Entity.json(createTask);

    @Context
    protected TaskService taskService;

    @Spy
    private TaskResource taskResource = new TaskResource();

    @Override
    public void configureMocks(MockingResourceConfig config) {
        config.register(taskResource);
    }

    @Override
    public void configureMocks(MockingAbstractBinder binder) {
        binder.bindFactory(Mock.mock(TaskServiceFactory.class)).to(TaskService.class).in(Singleton.class);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Test
    @Request(url = "task", method = GET, expectedStatus = {200, 204})
    public void getAllTasks(Response resp) throws Exception {
        verify(taskResource, 1).getAllTasks();
        verify(taskService, 1).getAllTasks();
    }

    @Test
    @Request(url = "task", method = POST, entity = "taskEntity", expectedStatus = {200, 204})
    public void createTask(Response resp) throws Exception {
        verify(taskResource, 1).createTask(Matchers.refEq(task));
        verify(taskService, 1).createTask(Matchers.refEq(task));
    }

    @Test
    @Request(url = "task/1", method = GET, expectedStatus = {200, 204})
    public void getTask(Response resp) throws Exception {
        verify(taskResource, 1).getTask(any(Task.class));
        verify(taskService, 1).getTask(any(Task.class));
    }

    @Test
    @Request(url = "task/1", method = PUT, entity = "taskEntity", expectedStatus = {200, 204})
    public void updateTask(Response resp) throws Exception {
        verify(taskResource, 1).updateTask(Matchers.refEq(task));
        verify(taskService, 1).updateTask(Matchers.refEq(task));
    }

    @Test
    @Request(url = "task/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteTask(Response resp) throws Exception {
        verify(taskResource, 1).deleteTask(any(Task.class));
        verify(taskService, 1).deleteTask(any(Task.class));
    }

}
