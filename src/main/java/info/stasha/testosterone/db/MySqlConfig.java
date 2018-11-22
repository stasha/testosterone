package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;

/**
 * MySql DbConfig implementation.
 *
 * @author stasha
 */
public class MySqlConfig extends AbstractDbConfig {

    public MySqlConfig() {
        this(null);
    }

    public MySqlConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "MySql DB";

        this.dataSource.setJdbcUrl("jdbc:mysql://localhost:5432/" + testDb);
        this.dataSource.setUsername(this.userName);
        this.dataSource.setPassword(this.password);

        // create/drop test db specific
        this.dbConnectionString = "jdbc:mysql://localhost:5432/postgres";
        this.createTestingDbSql = "CREATE DATABASE " + testDb + " WITH ENCODING='UTF8' OWNER = " + userName + " CONNECTION LIMIT=-1";
        this.dropTestingDbSql = "DROP DATABASE " + testDb;

    }

}
