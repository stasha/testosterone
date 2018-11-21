package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * H2 DbConfig implementation.
 *
 * @author stasha
 */
public class H2Config extends AbstractDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2Config.class);

    private JdbcConnectionPool connectionPool;

    public H2Config() {
    }

    public H2Config(TestConfig testConfig) {
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
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        if (!isRunning()) {
            LOGGER.info("Starting H2 DB.");
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
            LOGGER.info("Stopping H2 DB.");
            getConnection().prepareStatement("shutdown").execute();
            this.connectionPool.dispose();
            this.dataSource = null;
        }
    }

}
