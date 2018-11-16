package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.CloseableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Connection factory used for creating new connections when injecting ingo
 * method or class.<br>
 *
 * @Context Connection
 *
 * @author stasha
 */
public class H2ConnectionFactory implements Factory<Connection> {

    private static final Logger LOGGER = LoggerFactory.getLogger(H2ConnectionFactory.class);

    @Context
    private CloseableService cs;

    @Context
    private DbConfig config;

    @Override
    public Connection provide() {

        Connection conn = config.getConnection();
        LOGGER.info("Creating new connection {}.", conn.toString());

        cs.add(() -> {
            dispose(conn);
        });
        return conn;
    }

    @Override
    public void dispose(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                LOGGER.info("Connection {} was successfully closed.", conn.toString());
            }
        } catch (SQLException ex) {
            LOGGER.error("Failed to close connection {}.", conn);
            throw new RuntimeException(ex);
        }
    }

}
