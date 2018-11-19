package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class H2ConfigTest implements Testosterone {

    private final H2Config CONFIG = new H2Config(Mockito.mock(TestConfig.class));

    @Context
    DbConfig dbConfig;

    @Test(expected = SQLException.class)
    public void failedToExecuteScript() throws SQLException {
        dbConfig.add("test", "select * from test");
        dbConfig.execute();
    }

    @Test
    public void dataSourceTest() throws SQLException {
        assertNotNull("Datasource should not be null", dbConfig.getDataSource());
    }

    @Test(expected = UnsupportedOperationException.class)
    @DontIntercept
    public void createTestingDbTest() throws SQLException {
        CONFIG.createTestingDb();
    }

    @Test(expected = UnsupportedOperationException.class)
    @DontIntercept
    public void dropTestingDbTest() throws SQLException {
        CONFIG.dropTestingDb();
    }

    @Test
    @DontIntercept
    public void initSqlTest() {
        CONFIG.add("testQuery", "select * from test");
        assertEquals("Query should equal", "select * from test", CONFIG.getInitSqls().get("testQuery"));
    }

    @Test
    @DontIntercept
    public void getH2FactoryTest() throws SQLException {
        assertEquals("Factory should equal", H2ConnectionFactory.class, CONFIG.getConnectionFactory());
    }

    @Test(expected = NullPointerException.class)
    @DontIntercept
    public void getConnectionTest() throws Throwable {
        try {
            CONFIG.getConnection();
        } catch (Exception ex) {
            throw ex.getCause();
        }
    }
}
