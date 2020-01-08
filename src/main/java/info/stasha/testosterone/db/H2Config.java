package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * H2 DbConfig implementation.
 *
 * @author stasha
 */
public class H2Config extends AbstractDbConfig {
    
    private final String conn = "jdbc:h2:mem:";

    public H2Config() {
        this(null);
    }

    public H2Config(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "H2 DB";

        this.dbConnectionString = conn + testDb + ";create=true";
        this.dataSource.setJdbcUrl(this.dbConnectionString);
        this.dataSource.setUsername(this.userName);
        this.dataSource.setPassword(this.password);
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        if (isRunning()) {
            try (Connection c = DriverManager.getConnection(dbConnectionString, userName, password)) {
                c.prepareStatement("shutdown").execute();
            }
            super.stop();
        }
    }

}
