package info.stasha.testosterone.jersey.junit4.db;

import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.db.H2Config;

/**
 *
 * @author stasha
 */
public class ExtendedH2Config extends H2Config {

    private final String conn = "jdbc:h2:mem:";

    public ExtendedH2Config() {
        this(null);
    }

    public ExtendedH2Config(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "Extended Derby DB";

        this.dbConnectionString = conn + testDb + ";create=true";
        this.createTestingDbSql = "create schema if not exists test";
        this.dropTestingDbSql = "drop schema if exists test";
    }

}
