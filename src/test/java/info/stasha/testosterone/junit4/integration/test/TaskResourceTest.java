package info.stasha.testosterone.junit4.integration.test;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.GenericMockitoFactory;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.integration.app.task.Task;
import info.stasha.testosterone.junit4.integration.app.task.TaskResource;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskService;
import javax.inject.Singleton;
import static javax.ws.rs.HttpMethod.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Matchers;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class TaskResourceTest implements Testosterone {

    protected final TaskResource taskResource = Mockito.spy(new TaskResource());
    protected final TaskService mockedTaskService = Mockito.spy(TaskService.class);
    protected final Task task = new Task(3L, "Title", "Description", false);
    public Entity taskEntity = Entity.json(task);

    @Override
    public void configure(ResourceConfig config) {
        config.register(taskResource);
    }

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(GenericMockitoFactory.mock(mockedTaskService)).to(TaskService.class).in(Singleton.class);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Context
    protected TaskService taskService;

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
