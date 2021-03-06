package info.stasha.testosterone.jersey.junit4.db;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.DbConfig;
import info.stasha.testosterone.db.PostgresConfig;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import static org.junit.Assert.assertEquals;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Postgres DB test
 *
 * @author stasha
 */
@Ignore("Tested only on local machine")
@RunWith(TestosteroneRunner.class)
@Configuration(dbConfig = PostgresConfig.class, runDb = true)
public class PostgresConfigTest implements Testosterone {

    @Context
    DbConfig config;

    String create = "CREATE TABLE people (\n"
            + "  id SERIAL NOT NULL PRIMARY KEY,\n"
            + "  first_name VARCHAR(56) NOT NULL,\n"
            + "  last_name VARCHAR(56),\n"
            + "  created_at TIMESTAMP,\n"
            + "  updated_at TIMESTAMP\n"
            + "  )";

    @Override
    public void configure(DbConfig config) {
        config.add("createPeopleTable", create);
    }

    @Override
    public void configureMocks(DbConfig config) {
        config.add("addJoeToPeopleTable",
                "insert into people "
                + "(first_name, last_name) values ('jon', 'doe'), ('jon', 'doe')");
        config.add("addJonMockToPeopleTable",
                "insert into people "
                + "(first_name, last_name) values ('jon', 'doe'), ('jon', 'doe')");
    }

    @Override
    public boolean onDbInit(String queryName, String query) {
        // skipping record with specified name
        return !queryName.equals("addJonMockToPeopleTable");
    }

    @Test
    public void dbtest2(@Context Connection conn) throws SQLException {
        try (ResultSet rs = conn.prepareStatement("select first_name, last_name from people").executeQuery()) {
            int count = 0;
            while (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);

                assertEquals("jon", firstName);
                assertEquals("doe", lastName);

                count++;
            }
            assertEquals("There should be 2 records in DB", 2, count);
        }

    }

}
