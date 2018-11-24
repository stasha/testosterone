package info.stasha.testosterone.jersey.junit4.db;

import info.stasha.testosterone.db.ConnectionFactory;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public class ConnectionFactoryTest {

     private Connection conn = Mockito.mock(Connection.class);
     
     @Test(expected = RuntimeException.class)
     public void testSqlException() throws SQLException{
         Mockito.doThrow(new SQLException("failed to close connection")).when(conn).close();
         
         new ConnectionFactory().dispose(conn);
     }
    
}
