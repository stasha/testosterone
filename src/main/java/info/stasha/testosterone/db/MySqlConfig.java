package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;

/**
 * MySql DbConfig implementation.<br>
 * NOTE: User with create db privileges must be used.
 *
 * @author stasha
 */
public class MySqlConfig extends AbstractDbConfig {

    private final String conn = "jdbc:mysql://localhost:3306/";

    public MySqlConfig() {
        this(null);
    }

    public MySqlConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "MySql DB";

        this.dataSource.setJdbcUrl(conn + testDb);
        this.dataSource.setUsername(this.userName);
        this.dataSource.setPassword(this.password);

        // create/drop test db specific
        this.dbConnectionString = conn;
        this.createTestingDbSql = "CREATE SCHEMA `" + testDb + "` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci";
        this.dropTestingDbSql = "DROP DATABASE " + testDb;

    }

}
