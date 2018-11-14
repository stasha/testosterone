package info.stasha.testosterone.junit4.integration.test.resource;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Mock;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.integration.app.task.Task;
import info.stasha.testosterone.junit4.integration.app.task.TaskResource;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskService;
import info.stasha.testosterone.junit4.integration.app.task.service.TaskServiceFactory;
import static javax.ws.rs.HttpMethod.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Testing TaskResource with mocks.
 *
 * @author stasha
 */
@Ignore
@RunWith(TestosteroneRunner.class)
public class TaskResourceInheritedTest extends TaskResource implements Testosterone {

    protected final Task task = new Task(3L, "Title", "Description", false);
    protected final Task createTask = new Task("Title", "Description", false);
    public Entity taskEntity = Entity.json(task);
    public Entity createTaskEntity = Entity.json(createTask);

    @Context
    protected TaskService taskService;

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bind(Mock.mock(TaskServiceFactory.class)).to(TaskService.class).in(RequestScoped.class).proxy(false).proxyForSameScope(false);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Test
    @Request(url = "task/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteTaskTest(Response resp) throws Exception {
        

        verify(taskService, 1).deleteTask(any(Task.class));
    }

}
