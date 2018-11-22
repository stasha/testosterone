package info.stasha.testosterone.jersey.junit4.db;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.DontIntercept;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.jersey.junit4.Testosterone;
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
@Configuration(runDb = true)
public class AbstractDbConfigTest implements Testosterone {

    private final H2Config CONFIG = new H2Config(Mockito.mock(TestConfig.class));

    @Context
    DbConfig dbConfig;

    @Test(expected = SQLException.class)
    public void failedToExecuteScript() throws SQLException {
        dbConfig.add("test", "select * from test");
        dbConfig.execute();
    }

    @Test
    @DontIntercept
    public void initSqlTest() {
        CONFIG.add("testQuery", "select * from test");
        assertEquals("Query should equal", "select * from test", CONFIG.getInitSqls().get("testQuery"));
    }

    @Test
    public void returnTestConfig() {
        assertNotNull("TestConfig should not be null", dbConfig.getTestConfig());
    }

    @Test
    public void setAndReturnTestConfig() {
        TestConfig tc = new JerseyTestConfig(this);
        dbConfig.setTestConfig(tc);
        assertEquals("TestConfig should equal", tc, dbConfig.getTestConfig());
    }

    @Test(expected = RuntimeException.class)
    @DontIntercept
    public void connectionException() {
        H2Config conf = Mockito.spy(CONFIG);
        Mockito.doReturn(null).when(conf).getDataSource();
        conf.getConnection();
    }

}
