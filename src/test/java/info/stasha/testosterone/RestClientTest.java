package info.stasha.testosterone;

import javax.ws.rs.client.Client;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.mockito.Mockito;

/**
 *
 * @author stasha
 */
public class RestClientTest {

    private final TestConfig tc = Mockito.mock(TestConfig.class);
    private final RestClient rc = Mockito.spy(new RestClient(tc));
    private final Client c = Mockito.spy(rc.client());

    @Test
    public void getClientTest() {
        assertNotNull("Should return client", rc.client());
    }

    @Test(expected = Exception.class)
    public void webTargetException() {
        rc.target();
    }

    @Test
    public void closeClient() {
        rc.closeClient();
    }

    @Test(expected = RuntimeException.class)
    public void closeClientException() {
        Mockito.doThrow(IllegalStateException.class).when(c).close();
        rc.closeClient(c);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void isRunningTest() {
        rc.isRunning();
    }

}
