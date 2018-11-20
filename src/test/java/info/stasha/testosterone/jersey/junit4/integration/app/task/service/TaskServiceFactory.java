package info.stasha.testosterone.jersey.junit4.integration.app.task.service;

import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

/**
 *
 * @author stasha
 */
public class TaskServiceFactory implements Factory<TaskService> {

    @Context
    private ServiceLocator locator;

    @Override
    public TaskService provide() {
        return locator.createAndInitialize(TaskServiceImpl.class);
    }

    @Override
    public void dispose(TaskService instance) {

    }
}
