package info.stasha.testosterone.db;

import java.sql.Connection;
import java.sql.SQLException;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public class H2ConnectionFactoryTest {

     private Connection conn = Mockito.mock(Connection.class);
     
     @Test(expected = RuntimeException.class)
     public void testSqlException() throws SQLException{
         Mockito.doThrow(new SQLException("failed to close connection")).when(conn).close();
         
         new H2ConnectionFactory().dispose(conn);
     }
    
}
