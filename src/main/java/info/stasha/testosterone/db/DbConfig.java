package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import org.glassfish.hk2.api.Factory;

/**
 * DB configuration.
 *
 * @author stasha
 */
public interface DbConfig {

    /**
     * Add SQL query to DbConfig queue.<br>
     * Scripts will be invoked after DB starts.
     *
     *
     * @param queryName
     * @param query
     * @return
     */
    DbConfig add(String queryName, String query);

    /**
     * Executes SQL queries that are in queue
     *
     * @return
     * @throws java.sql.SQLException
     */
    DbConfig execute() throws SQLException;

    /**
     * Returns map of SQL queries that will be invoked after DB starts.
     *
     * @return
     */
    Map<String, String> getInitSqls();

    /**
     * Initialize database.
     *
     * @throws java.sql.SQLException
     */
    void init() throws SQLException;

    /**
     * Returns DataSource.
     *
     * @return
     */
    DataSource getDataSource();

    /**
     * Returns Connection to DB.
     *
     * @return
     */
    Connection getConnection();

    /**
     * Factory for creating connections.
     *
     * @return
     */
    Class<? extends Factory<Connection>> getConnectionFactory();

    /**
     * Method for creating testing DB or schema.
     *
     * @throws SQLException
     */
    void createTestingDb() throws SQLException;

    /**
     * Method for dropping testing DB or schema.
     *
     * @throws SQLException
     */
    void dropTestingDb() throws SQLException;

    /**
     * Starts DB server.
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Stops DB server.
     *
     * @throws Exception
     */
    void stop() throws Exception;
}
