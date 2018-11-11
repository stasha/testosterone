package info.stasha.testosterone.junit4.integration.app.task;

import info.stasha.testosterone.junit4.integration.app.task.service.TaskService;
import java.util.List;
import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 *
 * @author stasha
 */
@Path("task")
@Consumes("application/json")
@Produces("application/json")
public class TaskResource {

    @Context
    private TaskService taskService;

    @GET
    public List<Task> getAllTasks() throws Exception {
        return taskService.getAllTasks();
    }

    @POST
    public void createTask(Task task) throws Exception {
        taskService.createTask(task);
    }

    @GET
    @Path("{id}")
    public Task getTask(@BeanParam Task task) throws Exception {
        return taskService.getTask(task);
    }

    @PUT
    @Path("{id}")
    public void updateTask(Task task) throws Exception {
        taskService.updateTask(task);
    }

    @DELETE
    @Path("{id}")
    public void deleteTask(@BeanParam Task task) throws Exception {
        taskService.deleteTask(task);
    }

}
