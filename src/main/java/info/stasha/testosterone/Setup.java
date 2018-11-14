package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.IntegrationContainer;
import info.stasha.testosterone.jersey.Testosterone;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.annotations.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Container holding different "metadata" belonging to single test class
 * instance.
 *
 * @author stasha
 */
@Service
public class Setup implements ContainerResponseFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(Setup.class);

    @Context
    private ServiceLocator locator;

    protected Integration integration;
    protected IntegrationContainer tests;
    protected Setup parent;
    protected Setup root;
    protected boolean suite;
    protected final Testosterone testosterone;
    protected final ServerConfig serverConfig;
    protected DbConfig dbConfig;
    protected boolean beforeServerStart;
    protected boolean afterServerStart;
    protected boolean beforeServerStop;
    protected boolean afterServerStop;
    protected boolean requestsAlreadInvoked;
    protected ExecutingTest executingTest;

    /**
     * Creates TestosteroneSetup.
     *
     * @param testosterone
     * @param serverConfig
     */
    public Setup(Testosterone testosterone, ServerConfig serverConfig) {
        if (testosterone == null) {
            throw new IllegalArgumentException("Testosterone instance can't be null");
        }
        if (serverConfig == null) {
            throw new IllegalArgumentException("Configuration instance can't be null");
        }
        this.testosterone = testosterone;
        this.serverConfig = serverConfig;
    }

    /**
     * Returns integration annotation.
     *
     * @return
     */
    public Integration getIntegration() {
        if (this.root == null) {
            return Utils.getAnnotation(this.testosterone, Integration.class);
        }
        return Utils.getAnnotation(this.root.getTestosterone(), Integration.class);
    }

    /**
     * List of test objects that are in integration.
     *
     * @return
     */
    public IntegrationContainer getTests() {
        return tests;
    }

    /**
     * Sets list of test objects that are in integration.
     *
     * @param tests
     */
    public void setTests(IntegrationContainer tests) {
        this.tests = tests;
    }

    /**
     * Returns testosterone test instance.
     *
     * @return
     */
    public Testosterone getTestosterone() {
        return testosterone;
    }

    /**
     * Returns serverConfig used by testosterone test class.
     *
     * @return
     */
    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    /**
     * Returns DB configuration.
     *
     * @return
     */
    public DbConfig getDbConfig() {
        return dbConfig;
    }

    /**
     * Sets DB configuration.
     *
     * @param dbConfig
     */
    public void setDbConfig(DbConfig dbConfig) {
        this.dbConfig = dbConfig;
    }

    /**
     * Returns root setup.
     *
     * @return
     */
    public Setup getRoot() {
        return root;
    }

    /**
     * Sets root setup.
     *
     * @param root
     */
    public void setRoot(Setup root) {
        if (this.root == null) {
            this.root = root;
        }
    }

    /**
     * Returns parent Setup. For now this is not used anywhere.
     *
     * @return
     */
    public Setup getParent() {
        return parent;
    }

    /**
     * Sets parent.
     *
     * @param parent
     */
    public void setParent(Setup parent) {
        this.parent = parent;
    }

    /**
     * Returns true/false if this setup is suite.
     *
     * @return
     */
    public boolean isSuite() {
        return suite;
    }

    /**
     * Invokes beforeServerStart on passed object.
     *
     * @param orig
     * @throws Exception
     */
    public void beforeServerStart(Testosterone orig) throws Exception {
        if (!this.beforeServerStart) {
            this.beforeServerStart = true;
            LOGGER.info("Invoking beforeServerStart.");
            orig.beforeServerStart();
        }
    }

    /**
     * Invokes afterServerStart on passed object.
     *
     * @param orig
     * @throws Exception
     */
    public void afterServerStart(Testosterone orig) throws Exception {
        if (!this.afterServerStart) {
            this.afterServerStart = true;
            LOGGER.info("Invoking afterServerStart.");
            orig.afterServerStart();
        }
    }

    /**
     * Invokes beforeServerStop on passed object.
     *
     * @param orig
     * @throws Exception
     */
    public void beforeServerStop(Testosterone orig) throws Exception {
        if (!this.beforeServerStop) {
            this.beforeServerStop = true;
            LOGGER.info("Invoking beforeServerStop.");
            orig.beforeServerStop();
        }
    }

    /**
     * Invokes afterServerStop on passed object.
     *
     * @param orig
     * @throws Exception
     */
    public void afterServerStop(Testosterone orig) throws Exception {
        if (!this.afterServerStop) {
            this.afterServerStop = true;
            LOGGER.info("Invoking afterServerStop.");
            orig.afterServerStop();
        }
    }

    /**
     * Returns if beforeServerStart was invoked.
     *
     * @return
     */
    public boolean isBeforeServerStart() {
        return beforeServerStart;
    }

    /**
     * Returns if afterServerStart was invoked.
     *
     * @return
     */
    public boolean isAfterServerStart() {
        return afterServerStart;
    }

    /**
     * Returns if beforeServerStop was invoked.
     *
     * @return
     */
    public boolean isBeforeServerStop() {
        return beforeServerStop;
    }

    /**
     * Returns if afterServerStop was invoked.
     *
     * @return
     */
    public boolean isAfterServerStop() {
        return afterServerStop;
    }

    /**
     * Clears all start/stop flags
     */
    public void clearFlags() {
        LOGGER.debug("Clearing all flags.");
        this.beforeServerStart = false;
        this.afterServerStart = false;
        this.beforeServerStop = false;
        this.afterServerStop = false;
    }

    public ServiceLocator getServiceLocator() {
        return locator;
    }

    public boolean isRequestsAlreadInvoked() {
        return requestsAlreadInvoked;
    }

    public void setRequestsAlreadInvoked(boolean requestsAlreadInvoked) {
        this.requestsAlreadInvoked = requestsAlreadInvoked;
    }

    public ExecutingTest getExecutingTest() {
        return executingTest;
    }

    public void setExecutingTest(ExecutingTest executingTest) {
        this.executingTest = executingTest;
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        if (locator != null && testosterone != null) {
            locator.inject(testosterone);
        }
    }

}
