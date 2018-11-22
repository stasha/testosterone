package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;

/**
 * H2 DbConfig implementation.
 *
 * @author stasha
 */
public class H2Config extends AbstractDbConfig {

    public H2Config() {
        this(null);
    }

    public H2Config(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "H2 DB";

        this.dataSource.setJdbcUrl("jdbc:h2:mem:" + testDb + ";create=true");
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
