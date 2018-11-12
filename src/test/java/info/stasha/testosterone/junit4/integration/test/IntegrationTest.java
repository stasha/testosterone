package info.stasha.testosterone.junit4.integration.test;

import info.stasha.testosterone.annotation.InjectTest;
import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.junit4.integration.test.dao.TaskDaoTest;
import info.stasha.testosterone.junit4.integration.test.service.TaskServiceTest;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.ServiceLocator;
import org.junit.After;
import org.junit.Before;

/**
 *
 * @author stasha
 */
@Integration({
    TaskResourceTest.class,
    TaskServiceTest.class,
    TaskDaoTest.class
})
public class IntegrationTest extends TaskResourceTest {

    @InjectTest
    TaskDaoTest daoTest;

    @Context
    ServiceLocator locator;

    @Override
    public <T> T verify(T mock, int invocations) {
        return mock;
    }

    @Before
    public void setUp() throws Exception {
        daoTest.setUp();
    }

    @After
    public void tearDown() throws Exception {
        daoTest.tearDown();
    }

}
