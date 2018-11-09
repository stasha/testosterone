package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
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

/**
 * H2 DbConfig implementation.
 *
 * @author stasha
 */
@Service
public class H2Config implements DbConfig, ApplicationEventListener {

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
			Logger.getLogger(H2Config.class.getName()).log(Level.SEVERE, null, ex);
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
		getDataSource();
	}

	/**
	 * {@inheritDoc }
	 *
	 * @throws Exception
	 */
	@Override
	public void stop() throws Exception {
		getConnection().prepareStatement("shutdown").execute();
		this.connectionPool.dispose();
		this.dataSource = null;
	}

	@Override
	public void onEvent(ApplicationEvent ae) {
		try {
			if (ae.getType() == INITIALIZATION_START) {
				start();
			} else if (ae.getType() == DESTROY_FINISHED) {
				stop();
			}
		} catch (Exception ex) {
			Logger.getLogger(H2Config.class.getName()).log(Level.SEVERE, null, ex);
			throw new RuntimeException(ex);
		}
	}

	@Override
	public RequestEventListener onRequest(RequestEvent re) {
		return null;
	}

}
