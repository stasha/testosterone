package info.stasha.testosterone.jersey.junit4.db;

import info.stasha.testosterone.db.DerbyConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.db.HsqlDbConfig;
import info.stasha.testosterone.db.MySqlConfig;
import info.stasha.testosterone.db.PostgresConfig;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class DbConfigInstancesTest {

    @Test
    public void h2NewInstanceTest() {
        H2Config conf = new H2Config();
    }

    @Test
    public void hsqlDbNewInstanceTest() {
        HsqlDbConfig conf = new HsqlDbConfig();
    }

    @Test
    public void derbyNewInstanceTest() {
        DerbyConfig conf = new DerbyConfig();
    }

    @Test
    public void mySqlNewInstanceTest() {
        MySqlConfig conf = new MySqlConfig();
    }

    @Test
    public void postgresNewInstanceTest() {
        PostgresConfig conf = new PostgresConfig();
    }

}
