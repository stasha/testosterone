package info.stasha.testosterone.jersey.junit4.db;

import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.DbConfig;
import info.stasha.testosterone.db.DerbyConfig;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Context;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Derby in-memory DB test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(dbConfig = DerbyConfig.class, runDb = true)
public class DerbyConfigTest implements Testosterone {

    @Context
    DbConfig config;

    /**
     * @throws Exception
     */
    @Override
    public void afterServerStart() throws Exception {
        String create = "CREATE TABLE people (\n"
                + "  id  int NOT NULL PRIMARY KEY GENERATED ALWAYS AS IDENTITY \n"
                + " (START WITH 1, INCREMENT BY 1),\n"
                + "  first_name VARCHAR(56) NOT NULL,\n"
                + "  last_name VARCHAR(56),\n"
                + "  created_at TIMESTAMP,\n"
                + "  updated_at TIMESTAMP\n"
                + "  )";

        try (Connection conn = config.getConnection()) {
            conn.prepareStatement(create).executeUpdate();
            conn.prepareStatement("insert into people (first_name, last_name) values ('jon', 'doe')").executeUpdate();
        }
    }

    @Override
    public void beforeServerStop() throws Exception {
        try (Connection conn = config.getConnection()) {
            conn.prepareStatement("drop table people").executeUpdate();

            boolean thrown = false;
            try {
                conn.prepareStatement("select * from people").execute();
            } catch (SQLException ex) {
                thrown = true;
            }

            assertTrue("Table people should not exist", thrown);
        }
    }

    @Test
    public void dbtest(@Context Connection conn) throws SQLException {
        assertEquals(1, conn.prepareStatement("insert into people (first_name, last_name) values ('jon', 'doe')").executeUpdate());
    }

    @Test
    public void dbtest2(@Context Connection conn) throws SQLException {
        try (ResultSet rs = conn.prepareStatement("select first_name, last_name from people").executeQuery()) {
            while (rs.next()) {
                String firstName = rs.getString(1);
                String lastName = rs.getString(2);
                assertEquals("jon", firstName);
                assertEquals("doe", lastName);
            }
        }

    }

}
