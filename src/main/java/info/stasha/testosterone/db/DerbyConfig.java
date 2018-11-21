package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.derby.jdbc.EmbeddedDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Derby DbConfig implementation.
 *
 * @author stasha
 */
public class DerbyConfig extends AbstractDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(DerbyConfig.class);

    public DerbyConfig() {
    }

    public DerbyConfig(TestConfig testConfig) {
        super(testConfig);
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public DataSource getDataSource() {
        if (dataSource == null) {
            EmbeddedDataSource ds = new EmbeddedDataSource();
            testDbName = String.valueOf(Math.random()).replace(".", "");
            ds.setDatabaseName("memory:" +testDbName);
            ds.setCreateDatabase("create");
            ds.setUser("sa");
            ds.setPassword("sa");
            dataSource = ds;
            LOGGER.info("Created new datasource {}", ds.toString());
        }
        return dataSource;
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        if (!isRunning()) {
            LOGGER.info("Starting Derby DB.");
            getDataSource();
            execute();
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        if (isRunning()) {
            LOGGER.info("Stopping Derby DB.");
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException ex) {
                if (!ex.getSQLState().equals("XJ015")) {
                    throw ex;
                }
            } finally {
                this.dataSource = null;
            }
        }
    }

}
