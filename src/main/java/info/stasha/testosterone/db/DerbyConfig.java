package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Derby DbConfig implementation.
 *
 * @author stasha
 */
public class DerbyConfig extends AbstractDbConfig {

    public DerbyConfig() {
        this(null);
    }

    public DerbyConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "Derby DB";

        this.dataSource.setJdbcUrl("jdbc:derby:memory:" + this.testDb + ";create=true");
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
            try {
                DriverManager.getConnection("jdbc:derby:;shutdown=true");
            } catch (SQLException ex) {
                if (!ex.getSQLState().equals("XJ015")) {
                    throw ex;
                }
            } finally {
                super.stop();
            }
        }
    }

}
