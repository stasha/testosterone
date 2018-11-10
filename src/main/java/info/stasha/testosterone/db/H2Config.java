package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import static org.glassfish.jersey.server.monitoring.ApplicationEvent.Type.DESTROY_FINISHED;
import static org.glassfish.jersey.server.monitoring.ApplicationEvent.Type.INITIALIZATION_START;
import org.glassfish.jersey.server.monitoring.ApplicationEventListener;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.h2.jdbcx.JdbcConnectionPool;
import org.h2.jdbcx.JdbcDataSource;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * H2 DbConfig implementation.
 *
 * @author stasha
 */
@Service
public class H2Config implements DbConfig, ApplicationEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(H2Config.class);

	protected String testDbName;
	protected DataSource dataSource;
	protected JdbcConnectionPool connectionPool;

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
		} catch (SQLException ex) {
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
		getDataSource();
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
	 * {@inheritDoc }
	 *
	 * @throws Exception
	 */
	@Override
	public void start() throws Exception {
		LOGGER.info("Starting H2 DB.");
		getDataSource();
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

	@Override
	public void onEvent(ApplicationEvent ae) {
		if (ae.getType() == INITIALIZATION_START) {
			try {
				start();
			} catch (Exception ex) {
				LOGGER.error("Failed to start H2 DB");
				throw new RuntimeException(ex);
			}
		} else if (ae.getType() == DESTROY_FINISHED) {
			try {
				stop();
			} catch (Exception ex) {
				LOGGER.error("Failed to stop H2 DB");
				throw new RuntimeException(ex);
			}
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent re) {
		return null;
	}

}
