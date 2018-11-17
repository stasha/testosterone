package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Integration;
import info.stasha.testosterone.jersey.Testosterone;
import java.io.IOException;
import java.util.Map;
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

    private final TestConfig config;
    private final Testosterone testosterone;

    private Integration integration;
    private Map<String, Testosterone> tests;
    private Setup parent;
    private Setup root;
    private boolean beforeServerStart;
    private boolean afterServerStart;
    private boolean beforeServerStop;
    private boolean afterServerStop;
    private boolean requestsAlreadInvoked;
    private TestInExecution testInExecution;

    public Setup(TestConfig config) {
        this.config = config;
        this.testosterone = config.getTest();
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
    public Map<String, Testosterone> getTests() {
        return tests;
    }

    /**
     * Sets list of test objects that are in integration.
     *
     * @param tests
     */
    public void setTests(Map<String, Testosterone> tests) {
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
        this.root = this.root == null ? root : this.root;
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
     * Returns ServiceLocator
     *
     * @return
     */
    public ServiceLocator getServiceLocator() {
        return locator;
    }

    /**
     * Returns true/false if all @Request/s on test method were invoked.
     *
     * @return
     */
    public boolean isRequestsAlreadInvoked() {
        return requestsAlreadInvoked;
    }

    /**
     * Sets if all @Request/s were invoked.
     *
     * @param requestsAlreadInvoked
     */
    public void setRequestsAlreadInvoked(boolean requestsAlreadInvoked) {
        this.requestsAlreadInvoked = requestsAlreadInvoked;
    }

    /**
     * Returns test in execution instance
     *
     * @return
     */
    public TestInExecution getTestInExecution() {
        return testInExecution;
    }

    /**
     * Sets test in execution instance.
     *
     * @param testInExecution
     */
    public void setTestInExecution(TestInExecution testInExecution) {
        this.testInExecution = testInExecution;
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
     * Clears all start/stop flags
     */
    public void clearFlags() {
        LOGGER.debug("Clearing all flags.");
        this.beforeServerStart = false;
        this.afterServerStart = false;
        this.beforeServerStop = false;
        this.afterServerStop = false;
    }

    /**
     * Filter that injects injectables.
     *
     * @param requestContext
     * @param responseContext
     * @throws IOException
     */
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        locator.inject(testosterone);
    }

    @Override
    public String toString() {
        return "Setup{"
                + ", beforeServerStart=" + beforeServerStart
                + ", afterServerStart=" + afterServerStart
                + ", beforeServerStop=" + beforeServerStop
                + ", afterServerStop=" + afterServerStop
                + ", requestsAlreadInvoked=" + requestsAlreadInvoked
                + ", testInExecution=" + testInExecution
                + "}";
    }

}
