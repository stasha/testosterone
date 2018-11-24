package info.stasha.testosterone.db;

import com.zaxxer.hikari.HikariDataSource;
import info.stasha.testosterone.SuperTestosterone;
import info.stasha.testosterone.TestConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
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
    private TestConfig testConfig;
    protected boolean running;

    protected final Map<String, String> sqls = new LinkedHashMap<>();

    protected HikariDataSource dataSource = new HikariDataSource();
    protected SuperTestosterone test;
    protected String dbName = "!!! SPECIFY \"dbName\" NAME IN YOUR CONFIGURATION !!!";
    protected String testDb = "test_db_" + String.valueOf(Math.random()).replace(".", "");
    protected String userName = "testosterone";
    protected String password = "password";

    // create/drop testing db specific
    protected String dbConnectionString;
    protected String createTestingDbSql;
    protected String dropTestingDbSql;

    public AbstractDbConfig(TestConfig testConfig) {
        if (testConfig != null) {
            this.testConfig = testConfig;
            this.test = (SuperTestosterone) testConfig.getTest();
        }
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
        return ConnectionFactory.class;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public HikariDataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Connection getConnection() {
        try {
            return getDataSource().getConnection();
        } catch (Exception ex) {
            LOGGER.error("Failed to obtain new connection from connection pool.");
            throw new RuntimeException(ex);
        }
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
        return running;
    }

    /**
     * {@inheritDoc }
     *
     * @throws SQLException
     */
    @Override
    public void createTestingDb() throws SQLException {
        if (!isRunning() && this.createTestingDbSql != null) {
            LOGGER.info("Creating test DB: {}", testDb);
            try (Connection c = DriverManager.getConnection(dbConnectionString, userName, password);
                    PreparedStatement ps = c.prepareStatement(this.createTestingDbSql)) {
                ps.executeUpdate();
                LOGGER.info("Successfully created test DB: {}", testDb);
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws SQLException
     */
    @Override
    public void dropTestingDb() throws SQLException {
        if (isRunning() && this.dropTestingDbSql != null) {
            LOGGER.info("Dropping test DB: {}", testDb);
            try (Connection c = DriverManager.getConnection(dbConnectionString, userName, password);
                    PreparedStatement ps = c.prepareStatement(this.dropTestingDbSql)) {
                ps.executeUpdate();
                LOGGER.info("Successfully dropped test DB: {}", testDb);
            }
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        if (!isRunning()) {
            LOGGER.info("Starting " + this.dbName);
            createTestingDb();
            this.running = true;

            execute();
            LOGGER.info(this.dbName + " successfully started");
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
            LOGGER.info("Stopping " + this.dbName);
            this.dataSource.close();
            dropTestingDb();
            this.running = true;
            LOGGER.info(this.dbName + " successfully stopped");
        }
    }

}
