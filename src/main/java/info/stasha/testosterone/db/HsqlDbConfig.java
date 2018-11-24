package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;

/**
 * HSQL DbConfig implementation.
 * NOTE: HsqlDbConfig causes Jersey 2.21, 2.22 to fail with stack overflow.
 *
 * @author stasha
 */
public class HsqlDbConfig extends AbstractDbConfig {

    public HsqlDbConfig() {
        this(null);
    }

    public HsqlDbConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "HSQL DB";

        this.dataSource.setJdbcUrl("jdbc:hsqldb:mem:" + testDb + ";create=true");
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
            getConnection().prepareStatement("shutdown").execute();
            super.stop();
        }
    }

}
