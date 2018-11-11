package info.stasha.testosterone.junit4.integration.test;

import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.junit4.integration.app.task.TaskResource;
import info.stasha.testosterone.junit4.integration.test.dao.TaskDaoTest;
import info.stasha.testosterone.junit4.integration.test.service.TaskServiceTest;
import info.stasha.testosterone.junit4.jersey.resource.ResourceTest;
import org.glassfish.jersey.server.ResourceConfig;

/**
 *
 * @author stasha
 */
@Integration({
    ResourceTest.class,
    TaskServiceTest.class,
    TaskDaoTest.class
})
public class IntegrationTest extends TaskResourceTest {

    @Override
    public void configure(ResourceConfig config) {
        config.register(TaskResource.class);
    }

//    @Override
//    public void configure(AbstractBinder binder) {
//    }
    @Override
    public <T> T verify(T mock, int invocations) {
        return mock;
    }

}
