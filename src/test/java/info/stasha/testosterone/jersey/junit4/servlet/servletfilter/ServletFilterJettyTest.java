package info.stasha.testosterone.jersey.junit4.servlet.servletfilter;

import info.stasha.testosterone.annotation.Request;
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.servlet.Filter;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.IOException;
import java.util.EnumSet;
import static javax.servlet.DispatcherType.FORWARD;
import static javax.servlet.DispatcherType.INCLUDE;
import static javax.servlet.DispatcherType.REQUEST;
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
 * Just a basic servlet filter test
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class ServletFilterJettyTest implements Testosterone {

    private AccessFilter filter = Mockito.spy(new AccessFilter());
    private AccessFilter filter2 = Mockito.spy(new AccessFilter());

    @Override
    public void configure(ServletContainerConfig sc) {
        sc.addFilter(filter, "/*");
        sc.addFilter(filter2, new String[]{"/*"});
        sc.addFilter(AccessFilter.class, "/*");
        sc.addFilter(AccessFilter.class, new String[]{"/*"});
        sc.addFilter(filter2, new String[]{"/test/*"}, EnumSet.of(FORWARD, INCLUDE, REQUEST));
        sc.addFilter(AccessFilter.class, new String[]{"/home/*", "/user/*"});
        sc.addFilter(AccessFilter.class, new String[]{"/home/*", "/user/*"}, EnumSet.of(FORWARD, INCLUDE, REQUEST));
        sc.addFilter(new Filter(AccessFilter.class));
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
