package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;

/**
 * HSQL DbConfig implementation.
 *
 * @author stasha
 */
public class HsqldbConfig extends AbstractDbConfig {

    public HsqldbConfig() {
        this(null);
    }

    public HsqldbConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "HSQL DB";

        this.dataSource.setJdbcUrl("jdbc:hsqldb:hsql:mem" + testDb + ";create=true");
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
