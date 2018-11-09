package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.CloseableService;

/**
 * Connection factory used for creating new connections when injecting ingo
 * method or class.<br>
 *
 * @Context Connection
 *
 * @author stasha
 */
public class H2ConnectionFactory implements Factory<Connection> {

	@Context
	private CloseableService cs;

	@Context
	private H2Config config;

	@Override
	public Connection provide() {

		Connection conn = config.getConnection();

		cs.add(() -> {
			try {
				if (conn != null && !conn.isClosed()) {
					conn.close();
				}
			} catch (SQLException ex) {
				Logger.getLogger(H2ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		});
		return conn;
	}

	@Override
	public void dispose(Connection t) {
	}

}
