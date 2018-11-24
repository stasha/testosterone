package info.stasha.testosterone.jersey.junit4.jersey.request;

import info.stasha.testosterone.jersey.junit4.*;
import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(runServer = false, baseUri="http://stasha.info/", httpPort = -1)
public class ExternalBaseUriWithCustomJerseyPathTest implements Testosterone {

    @Override
    public void configure(ServletContainerConfig config) {
        config.setJaxRsPath("/jersey/*");
    }
    
    

    @Test
    @Request(url = "fdsafds", expectedStatus = 404)
    public void test(Response resp) {
    }
}
