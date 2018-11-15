package info.stasha.testosterone.junit4.db;

import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.junit4.*;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.jersey.Testosterone;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * H2 in-memory DB test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class DbInitSqlTest implements Testosterone {

    @Context
    H2Config config;

    String create = "CREATE TABLE people (\n"
            + "  id  int(11) NOT NULL auto_increment PRIMARY KEY,\n"
            + "  first_name VARCHAR(56) NOT NULL,\n"
            + "  last_name VARCHAR(56),\n"
            + "  created_at DATETIME,\n"
            + "  updated_at DATETIME\n"
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
