package info.stasha.testosterone.jersey.junit4;

import info.stasha.testosterone.annotation.Configuration;
import info.stasha.testosterone.jersey.junit4.servlet.AccessServlet;
import info.stasha.testosterone.jersey.junit4.servlet.servletfilter.AccessFilter;
import info.stasha.testosterone.jersey.junit4.servlet.servletlistener.ServletListener;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servers.TomcatServerConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Http methods test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
@Configuration(serverConfig = TomcatServerConfig.class)
public class PlaygroundTest implements Testosterone {

    @Override
    public void configure(ServletContainerConfig config) {
//        config.addServlet(new AccessServlet(), "/*");
//        config.addFilter(new AccessFilter(), "/*");
        config.addListener(new ServletListener());
    }

    @Test
    public void test() {
        System.out.println("test");
    }

}
