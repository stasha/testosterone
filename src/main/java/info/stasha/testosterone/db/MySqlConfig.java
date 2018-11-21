package info.stasha.testosterone.db;

import com.mysql.cj.jdbc.MysqlDataSource;
import info.stasha.testosterone.TestConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Postgres DbConfig implementation.
 *
 * @author stasha
 */
public class MySqlConfig extends AbstractDbConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MySqlConfig.class);

    protected String host = "localhost";
    protected int port = 3306;
    protected String dbname = "mysql";
    protected String url = "jdbc:mysql://" + host + ":" + port + "/" + dbname;
    protected String userName = "testosterone";
    protected String password = "password";

    public MySqlConfig() {
    }

    public MySqlConfig(TestConfig testConfig) {
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
            MysqlDataSource ds = new MysqlDataSource();
            ds.setDatabaseName(testDbName);
            ds.setServerName(host);
            ds.setPortNumber(port);
            ds.setUser(userName);
            ds.setPassword(password);
            dataSource = ds;
            LOGGER.info("Created new datasource {}", ds.toString());
        }
        return dataSource;
    }

    @Override
    public void createTestingDb() throws SQLException {
        if (!isRunning()) {
            testDbName = "test_db_" + String.valueOf(Math.random()).replace(".", "");
            LOGGER.info("Creating test DB: {}", testDbName);
            try (Connection c = DriverManager.getConnection(url, userName, password);
                    PreparedStatement ps = c.prepareStatement("CREATE DATABASE " + testDbName + " WITH ENCODING='UTF8' OWNER = " + userName + " CONNECTION LIMIT=-1")) {
                ps.executeUpdate();
                LOGGER.info("Successfully created test DB: {}", testDbName);
            }
        }
    }

    @Override
    public void dropTestingDb() throws SQLException {
        if (isRunning()) {
            LOGGER.info("Dropping test DB: {}", testDbName);
            try (Connection c = DriverManager.getConnection(url, userName, password);
                    PreparedStatement ps = c.prepareStatement("DROP DATABASE " + testDbName)) {
                ps.executeUpdate();
                LOGGER.info("Successfully dropped test DB: {}", testDbName);
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
            LOGGER.info("Start using Postgresql DB.");
            createTestingDb();
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
            dropTestingDb();
            LOGGER.info("End using Postgresql DB.");
            this.dataSource = null;
        }
    }

}
