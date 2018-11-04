package info.stasha.testosterone.jersey;

import info.stasha.testosterone.Instrument;
import info.stasha.testosterone.Suite;
import info.stasha.testosterone.annotation.Configuration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;

import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import info.stasha.testosterone.servlet.ServletContainerConfig;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author stasha
 */
public interface Testosterone {

	public static class Setup {

		private Setup parent;
		private boolean suite;
		private final Testosterone testosterone;
		private final JerseyConfiguration configuration;

		public Setup(Testosterone testosterone, JerseyConfiguration configuration, boolean isSuite) {
			if (testosterone == null) {
				throw new IllegalArgumentException("Testosterone instance can't be null");
			}
			if (configuration == null) {
				throw new IllegalArgumentException("Configuration instance can't be null");
			}
			this.testosterone = testosterone;
			this.configuration = configuration;
			this.suite = isSuite;
		}

		public Testosterone getTestosterone() {
			return testosterone;
		}

		public JerseyConfiguration getConfiguration() {
			return configuration;
		}

		public Setup getParent() {
			return parent;
		}

		public void setParent(Setup parent) {
			this.parent = parent;
		}

		public boolean isSuite() {
			return suite;
		}

		public void setSuite(boolean suite) {
			this.suite = suite;
		}

	}

	Logger LOGGER = Logger.getLogger(Testosterone.class.getName());

	Map<String, Setup> SETUP = new HashMap<>();

	default Set<Throwable> getMessages() {
		return getConfiguration().getMessages();
	}

	default void throwErrorMessage() throws Throwable {
		if (getMessages().size() > 0) {
			throw getMessages().iterator().next();
		}
	}

	default List<Throwable> getExpectedExceptions() {
		return getConfiguration().getExpectedExceptions();
	}

	default void throwExpectedException() throws Throwable {
		if (getExpectedExceptions().size() > 0) {
			throw getExpectedExceptions().iterator().next();
		}
	}

	default WebTarget target() {
		return getConfiguration().target();
	}

	default WebTarget target(String path) {
		return target().path(path);
	}

	default Client client() {
		return getConfiguration().client();
	}

	default void setConfiguration(JerseyConfiguration config) {
		Configuration ca = this.getClass().getAnnotation(Configuration.class);
		if (ca != null) {
			config.setBaseUri(ca.baseUri());
			config.setPort(ca.port());
			config.setServerStarts(ca.serverStarts());
			config.setManagedByParentConfiguration(false);
		}

		if (!SETUP.containsKey(this.getClass().getName())) {
			SETUP.put(this.getClass().getName(), new Setup(this, config, this.getClass().isAnnotationPresent(SuiteClasses.class)));
		}
	}

	default JerseyConfiguration getConfiguration() {
		return getSetup().getConfiguration();
	}

	default Setup getSetup() {
		if (!SETUP.containsKey(this.getClass().getName())) {
			try {
				info.stasha.testosterone.annotation.Configuration c
						= this.getClass().getAnnotation(info.stasha.testosterone.annotation.Configuration.class);
				JerseyConfiguration conf;
				if (c != null) {
					conf = (JerseyConfiguration) c.configuration().newInstance();
				} else {
					conf = new JettyConfiguration();
				}

				setConfiguration(conf);

			} catch (InstantiationException | IllegalAccessException ex) {
				Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
				throw new RuntimeException(ex);
			}
		}
		return SETUP.get(this.getClass().getName());
	}

	default void configure(ResourceConfig config) {

	}

	default void configure(AbstractBinder binder) {

	}

	default void configure(ServletContainerConfig config) {

	}

	default void configure(ResourceConfig config, ServletContainerConfig servletConfig) {

	}

	/**
	 * Suite classes.
	 *
	 * @param suite
	 */
	default void configure(Suite suite) {

	}

	default void initConfiguration() {
		configure(getConfiguration().getResourceConfig());
		getConfiguration().getResourceConfig().register(new AbstractBinder() {
			@Override
			protected void configure() {
				Testosterone.this.configure(this);
			}
		});

		configure(getConfiguration().getServletContainerConfig());

		configure(getConfiguration().getResourceConfig(),
				getConfiguration().getServletContainerConfig());

		getConfiguration().initConfiguration(this);

		SuiteClasses classes = this.getClass().getAnnotation(SuiteClasses.class);
		if (classes != null) {
			List<Class<?>> cls = Arrays.asList(classes.value());
			Collections.reverse(cls);
			List<Testosterone> tss = new ArrayList<>();
			for (Class<?> t : cls) {
				if (!t.isAnnotationPresent(Configuration.class)) {
					t = Instrument.testClass(t, null, null, null, null);
					try {
						JerseyConfiguration conf = getConfiguration().getClass().newInstance();
						conf.setResourceConfig(getConfiguration().getResourceConfig());
						conf.setServletContainerConfig(getConfiguration().getServletContainerConfig());
						conf.setManagedByParentConfiguration(true);

						setConfiguration(conf);

						Testosterone ts = ((Testosterone) t.newInstance());
						ts.setConfiguration(conf);

						ts.configure(getConfiguration().getResourceConfig());
						ts.configure(getConfiguration().getServletContainerConfig());
						ts.configure(getConfiguration().getResourceConfig(), getConfiguration().getServletContainerConfig());

						tss.add(ts);

						ts.initConfiguration();

						ts.getSetup().setParent(getSetup());

					} catch (InstantiationException | IllegalAccessException ex) {
						Logger.getLogger(Testosterone.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
			}
			getConfiguration().getResourceConfig().register(new AbstractBinder() {
				@Override
				protected void configure() {
					for (Testosterone ts : tss) {
						ts.configure(this);
					}
				}
			});

//			if (getConfiguration().serverStarts() != Configuration.ServerStarts.PARENT_CONFIGURATION) {
//				getConfiguration().setServerStarts(Configuration.ServerStarts.DONT_START);
//			}
		}
	}

	default void start() throws Exception {
		if (!getConfiguration().isManagedByParentConfiguration() && !getConfiguration().isRunning()) {
			System.out.println("");
			getConfiguration().stop();
			initConfiguration();
			if (!getSetup().isSuite() || getSetup().isSuite()
					&& getConfiguration().serverStarts() == Configuration.ServerStarts.PARENT_CONFIGURATION) {
				getConfiguration().init();
				getConfiguration().start();
			}
		}
	}

	default void stop() throws Exception {
		if (!getConfiguration().isManagedByParentConfiguration() && getConfiguration().isRunning()) {
			SuiteClasses sc = this.getClass().getAnnotation(SuiteClasses.class);
			if (sc != null) {
				for (Class<?> cls : sc.value()) {
					SETUP.remove(Instrument.testClass(cls, null, null, null, null).getName());
				}
			}
			getConfiguration().stop();
			System.out.println("");
			afterServerStop();
		}
	}

	default void beforeTest() throws Exception {
		start();
	}

	default void afterTest() throws Exception {
		stop();
	}

	default void afterServerStop() {

	}

}
