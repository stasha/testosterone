package info.stasha.testosterone.junit4.servlet.servletfilter;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.IOException;
import javax.inject.Singleton;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.core.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mockito;
import static org.mockito.Mockito.times;

/**
 * Servlet filter test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletFilterTest implements Testosterone {

    private AccessFilter filter = Mockito.spy(new AccessFilter());

    @Override
    public void configure(ServletContainerConfig sc) {
        sc.addFilter(filter, "/*");
    }

    @Override
    public void afterServerStop() {
        Mockito.verify(filter, times(1)).destroy();
    }

    @Test
    @Request(url = "/", expectedStatus = 404)
    public void test(Response resp) throws ServletException, IOException {
        Mockito.verify(filter, times(1)).init(any(FilterConfig.class));

        // There will be two "doFilter" calls, one by test initialization request 
        // and other by @Request annotation
        Mockito.verify(filter, times(2)).doFilter(
                any(ServletRequest.class),
                any(ServletResponse.class),
                any(FilterChain.class));
    }

}
