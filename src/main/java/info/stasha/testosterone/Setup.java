package info.stasha.testosterone;

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

    private final TestConfig config;

    private boolean beforeServerStart;
    private boolean afterServerStart;
    private boolean beforeServerStop;
    private boolean afterServerStop;
    private boolean requestsAlreadInvoked;
    private TestInExecution testInExecution;

    public Setup(TestConfig config) {
        this.config = config;
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
    public void beforeServerStart(SuperTestosterone orig) throws Exception {
        if (!isBeforeServerStart()) {
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
    public void afterServerStart(SuperTestosterone orig) throws Exception {
        if (!isAfterServerStart()) {
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
    public void beforeServerStop(SuperTestosterone orig) throws Exception {
        if (!isBeforeServerStop()) {
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
    public void afterServerStop(SuperTestosterone orig) throws Exception {
        if (!isAfterServerStop()) {
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
        locator.inject(config.getTest());
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
