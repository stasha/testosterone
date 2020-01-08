package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * HSQL DbConfig implementation. NOTE: HsqlDbConfig causes Jersey 2.21, 2.22 to
 * fail with stack overflow.
 *
 * @author stasha
 */
public class HsqlDbConfig extends AbstractDbConfig {

    private final String conn = "jdbc:hsqldb:mem:";

    public HsqlDbConfig() {
        this(null);
    }

    public HsqlDbConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "HSQL DB";

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
