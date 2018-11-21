package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import javax.sql.DataSource;
import org.hsqldb.jdbc.JDBCDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HSQL DbConfig implementation.
 *
 * @author stasha
 */
public class HsqldbConfig extends AbstractDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(HsqldbConfig.class);

    public HsqldbConfig() {
    }

    public HsqldbConfig(TestConfig testConfig) {
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
            JDBCDataSource ds = new JDBCDataSource();
            testDbName = String.valueOf(Math.random()).replace(".", "");
            ds.setDatabaseName(testDbName);
            ds.setUrl("jdbc:hsqldb:hsql:mem" + testDbName);
            ds.setUser("sa");
            ds.setPassword("sa");
            ds.setDatabase("true");
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
            LOGGER.info("Starting HSQL DB.");
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
            LOGGER.info("Stopping HSQL DB.");
            getConnection().prepareStatement("shutdown").execute();
            this.dataSource = null;
        }
    }

}
