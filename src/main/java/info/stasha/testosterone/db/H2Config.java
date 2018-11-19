package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.jersey.Testosterone;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.sql.DataSource;
import org.glassfish.hk2.api.Factory;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * H2 DbConfig implementation.
 *
 * @author stasha
 */
public class H2Config implements DbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2Config.class);
    private final Map<String, String> sqls = new LinkedHashMap<>();

    private String testDbName;
    private DataSource dataSource;
    private JdbcConnectionPool connectionPool;

    protected Testosterone test;

    private final TestConfig config;

    public H2Config(TestConfig config) {
        this.config = config;
        this.test = config.getTest();
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
    public DataSource getDataSource() {
        if (dataSource == null) {
            JdbcDataSource ds = new JdbcDataSource();
            testDbName = String.valueOf(Math.random()).replace(".", "");
            ds.setURL("jdbc:h2:mem:" + testDbName);
            ds.setUser("sa");
            ds.setPassword("sa");
            dataSource = ds;
            connectionPool = JdbcConnectionPool.create(ds);
            LOGGER.info("Created new datasource {}", ds.toString());
        }
        return dataSource;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Connection getConnection() {
        try {
            return connectionPool.getConnection();
        } catch (Exception ex) {
            LOGGER.error("Failed to obtain new connection from connection pool.");
            throw new RuntimeException(ex);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Class<? extends Factory<Connection>> getConnectionFactory() {
        return H2ConnectionFactory.class;
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
     */
    @Override
    public void init() throws SQLException {
        execute();
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        LOGGER.info("Starting H2 DB.");
        getDataSource();
        init();
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        LOGGER.info("Stopping H2 DB.");
        getConnection().prepareStatement("shutdown").execute();
        this.connectionPool.dispose();
        this.dataSource = null;
    }

}
