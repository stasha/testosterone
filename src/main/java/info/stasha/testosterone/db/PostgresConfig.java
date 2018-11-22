package info.stasha.testosterone.db;

import info.stasha.testosterone.TestConfig;

/**
 * Postgres DbConfig implementation.
 *
 * @author stasha
 */
public class PostgresConfig extends AbstractDbConfig {

    public PostgresConfig() {
        this(null);
    }

    public PostgresConfig(TestConfig testConfig) {
        super(testConfig);
        this.dbName = "Postgres DB";

        this.dataSource.setJdbcUrl("jdbc:postgresql://localhost:5432/" + testDb);
        this.dataSource.setUsername(this.userName);
        this.dataSource.setPassword(this.password);

        // create/drop test db specific
        this.dbConnectionString = "jdbc:postgresql://localhost:5432/postgres";
        this.createTestingDbSql = "CREATE DATABASE " + testDb + " WITH ENCODING='UTF8' OWNER = " + userName + " CONNECTION LIMIT=-1";
        this.dropTestingDbSql = "DROP DATABASE " + testDb;
    }

}
