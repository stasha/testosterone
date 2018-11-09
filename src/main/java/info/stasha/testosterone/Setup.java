package info.stasha.testosterone;

import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.jersey.Testosterone;
import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.ServiceLocator;
import org.glassfish.jersey.server.monitoring.ApplicationEvent;
import org.glassfish.jersey.server.monitoring.RequestEvent;
import org.glassfish.jersey.server.monitoring.RequestEventListener;
import org.jvnet.hk2.annotations.Service;

/**
 * Container holding different "metadata" belonging to single test class
 * instance.
 *
 * @author stasha
 */
@Service
public class Setup implements ContainerResponseFilter {

	@Context
	private ServiceLocator locator;

	protected Setup parent;
	protected boolean suite;
	protected final Testosterone testosterone;
	protected final ServerConfig serverConfig;
	protected DbConfig dbConfig;
	protected boolean beforeServerStart;
	protected boolean afterServerStart;
	protected boolean beforeServerStop;
	protected boolean afterServerStop;

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
		this.beforeServerStart = false;
		this.afterServerStart = false;
		this.beforeServerStop = false;
		this.afterServerStop = false;
	}

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
		if (locator != null && testosterone != null) {
			locator.inject(testosterone);
		}
	}

}
