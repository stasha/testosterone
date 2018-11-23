package info.stasha.testosterone.jersey.junit4.servlet;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.jersey.junit4.jersey.resource.Resource;
import info.stasha.testosterone.servlet.Servlet;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Just a basic servlet test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletJettyTest implements Testosterone {

    AccessServlet servlet = Mockito.spy(new AccessServlet());
    AccessServlet servlet2 = new AccessServlet();

    @Override
    public void configure(ServletContainerConfig sc) {
        sc.setJerseyServletPath("/*").addServlet(servlet, "/hello/*");
        sc.addServlet(servlet2, new String[]{"/admins/a/*", "/adming/b/*"});
        sc.addServlet(servlet2, new String[]{"/users/a/*", "/users/b/*"});
        sc.addServlet(AccessServlet.class, "/home/a/*");
        sc.addServlet(AccessServlet.class, new String[]{"/home/b/*", "/home/c/*"})
                .addInitParam("init", "init value").setInitOrder(1);
        sc.addServlet(new Servlet(AccessServlet.class));
    }

    @Test
    @Request(url = "/hello/" +Resource.HELLO_WORLD_PATH, expectedStatus = 405)
    public void test(Response resp) throws ServletException, IOException {
        Mockito.verify(servlet, times(1)).service(any(HttpServletRequest.class), any(HttpServletResponse.class));
    }

}
