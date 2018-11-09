package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.glassfish.hk2.api.Factory;

/**
 * DB configuration.
 *
 * @author stasha
 */
public interface DbConfig {

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
