package info.stasha.testosterone.junit4.integration.test;

import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.integration.app.task.Task;
import info.stasha.testosterone.junit4.integration.app.task.TaskResource;
import info.stasha.testosterone.junit4.integration.test.dao.TaskDaoTest;
import info.stasha.testosterone.junit4.integration.test.service.TaskServiceTest;
import static javax.ws.rs.HttpMethod.DELETE;
import static javax.ws.rs.HttpMethod.GET;
import static javax.ws.rs.HttpMethod.POST;
import static javax.ws.rs.HttpMethod.PUT;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@Integration({
    TaskServiceTest.class,
    TaskDaoTest.class
})
@RunWith(TestosteroneRunner.class)
public class TaskEndpointIntegrationTest implements Testosterone {

    protected TaskResource taskResource;
    protected final Task task = new Task(3L, "Title", "Description", false);
    protected final Task createTask = new Task("Title", "Description", false);
    public Entity taskEntity = Entity.json(task);
    public Entity createTaskEntity = Entity.json(createTask);

    @InjectTest
    TaskDaoTest daoTest;

    @Override
    public void configure(ResourceConfig config) {
        config.register(TaskResource.class);
    }

    @Before
    public void setUp() throws Exception {
        daoTest.setUp();
    }

    @After
    public void tearDown() throws Exception {
        daoTest.tearDown();
    }

    @Test
    @Request(url = "task", method = GET, expectedStatus = {200, 204})
    public void getAllTasks(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task", method = POST, entity = "taskEntity", expectedStatus = {500})
    public void failCreateTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task", method = POST, entity = "createTaskEntity", expectedStatus = {200, 204})
    public void createTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task/1", method = GET, expectedStatus = {200, 204})
    public void getTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task/1", method = PUT, entity = "taskEntity", expectedStatus = {200, 204})
    public void updateTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteTask(Response resp) throws Exception {
    }

}
