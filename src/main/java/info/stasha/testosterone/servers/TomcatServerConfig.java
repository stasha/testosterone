package info.stasha.testosterone.servers;

import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.io.File;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.core.StandardHost;
import org.apache.catalina.core.StandardWrapper;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.startup.Tomcat.ExistingStandardWrapper;

/**
 *
 * @author stasha
 */
public class TomcatServerConfig implements ServerConfig {

    protected Tomcat server;
    private TestConfig testConfig;
    private ServletContainerConfig servletContainerConfig;

    public TomcatServerConfig() {
        this(null);
    }

    public TomcatServerConfig(TestConfig config) {
        this.testConfig = config;
    }

    @Override
    public ServletContainerConfig getServletContainerConfig() {
        return this.servletContainerConfig;
    }

    @Override
    public void setServletContainerConfig(ServletContainerConfig config) {
        this.servletContainerConfig = config;
    }

    @Override
    public void setConfigurationObject(Object configuration) {

    }

    @Override
    public TestConfig getTestConfig() {
        return this.testConfig;
    }

    @Override
    public void setTestConfig(TestConfig testConfig) {
        this.testConfig = testConfig;
    }

    @Override
    public void start() throws Exception {
        server = new Tomcat();
        server.setPort(this.getTestConfig().getHttpPort());

        StandardContext ctx = new StandardContext();
        ctx.setDocBase(new File(".").getAbsolutePath());
        ctx.setPath("");
        ctx.setName("");
        ctx.addParameter("test", "test");
        ctx.addLifecycleListener(new Tomcat.FixContextListener());
        ctx.setParent(server.getHost());


        Wrapper sw = new StandardWrapper();
        sw.setName("servlet");
        sw.addInitParameter("test", "test");
        sw.setLoadOnStartup(1);
        sw.setServletClass(this.getServletContainerConfig().getServlets().iterator().next().getServlet().getClass().getName());
        sw.setServlet(this.getServletContainerConfig().getServlets().iterator().next().getServlet());
        
        ctx.addChild(sw);
        ctx.addServletMappingDecoded("/*", "servlet");
        
//        ctx.getServletContext().addServlet("servlet", );
//        ctx.addServletMappingDecoded("/*", "servlet");
        server.getHost().addChild(ctx);
        
        
        
//        server.addContext(host, "", new File(".").getAbsolutePath());
        

//        Context ctx = server.addContext("", new File(".").getAbsolutePath());

        
//        Tomcat.addServlet(ctx, "servlet", this.getServletContainerConfig().getServlets().iterator().next().getServlet());
//        ctx.addServletMappingDecoded("/*", "servlet");
        
//        FilterDef f = new FilterDef();
//        f.setFilter(this.getServletContainerConfig().getFilters().iterator().next().getFilter());
//        f.setFilterName("filterName");
//        FilterMap fm = new FilterMap();
//        fm.setFilterName("filterName");
//        fm.addURLPattern("/*");
//        ctx.addFilterDef(f);
//        ctx.addFilterMap(fm);
        
//        LifecycleListenerRule r = new LifecycleListenerRule(listenerClass, attributeName)
        
        
        server.start();
    }

    @Override
    public void stop() throws Exception {
//        server.stop();
        server = null;
    }

    @Override
    public boolean isRunning() {
        return server != null;
    }

}
