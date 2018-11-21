package info.stasha.testosterone.db;

import info.stasha.testosterone.SuperTestosterone;
import info.stasha.testosterone.TestConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.glassfish.hk2.api.Factory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract DbConfig implementation.
 *
 * @author stasha
 */
public abstract class AbstractDbConfig implements DbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractDbConfig.class);
    protected final Map<String, String> sqls = new LinkedHashMap<>();

    protected String testDbName;
    protected DataSource dataSource;
    protected SuperTestosterone test;
    private TestConfig testConfig;

    public AbstractDbConfig() {
    }

    public AbstractDbConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
        this.test = (SuperTestosterone) testConfig.getTest();
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public TestConfig getTestConfig() {
        return this.testConfig;
    }

    /**
     * {@inheritDoc }
     *
     * @param testConfig
     */
    @Override
    public void setTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
        this.test = (SuperTestosterone) testConfig.getTest();
    }

    /**
     * {@inheritDoc }
     *
     * @param queryName
     * @param query
     * @return
     */
    @Override
    public DbConfig add(String queryName, String query) {
        sqls.put(queryName, query);
        return this;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Map<String, String> getInitSqls() {
        return sqls;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Class<? extends Factory<Connection>> getConnectionFactory() {
        return DbConnectionFactory.class;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Connection getConnection() {
        try {
            return this.dataSource.getConnection();
        } catch (Exception ex) {
            LOGGER.error("Failed to obtain new connection from connection pool.");
            throw new RuntimeException(ex);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws SQLException
     */
    @Override
    public void createTestingDb() throws SQLException {
        throw new UnsupportedOperationException("Testing DB is created on server startup");
    }

    /**
     * {@inheritDoc }
     *
     * @throws SQLException
     */
    @Override
    public void dropTestingDb() throws SQLException {
        throw new UnsupportedOperationException("Testing DB is dropped on server end");
    }

    /**
     * Executes all SQL queries in the queue and removes them after execution.
     *
     * @return
     * @throws SQLException
     */
    @Override
    public DbConfig execute() throws SQLException {
        if (!sqls.isEmpty()) {
            try (Connection conn = getConnection()) {
//                conn.setAutoCommit(false);
                Statement st = conn.createStatement();
                for (String queryName : sqls.keySet()) {
                    String query = sqls.get(queryName);
                    if (test.onDbInit(queryName, query)) {
                        LOGGER.info("Executing SQL {}.", queryName);
                        st.execute(sqls.get(queryName));
                    } else {
                        LOGGER.info("Skipping SQL {}.", queryName);
                    }
                }

                sqls.clear();
                LOGGER.info("SQL scripts executed successfully.");
            } catch (SQLException ex) {
                LOGGER.error("Failed to execute SQL query.", ex);
                throw ex;
            }
        }
        return this;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        return this.dataSource != null;
    }

}
