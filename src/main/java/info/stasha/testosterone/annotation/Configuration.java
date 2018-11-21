package info.stasha.testosterone.annotation;

import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.StartServer;
import info.stasha.testosterone.TestConfig;
import info.stasha.testosterone.db.DbConfig;
import info.stasha.testosterone.db.H2Config;
import info.stasha.testosterone.jersey.JerseyTestConfig;
import info.stasha.testosterone.servers.JettyServerConfig;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Test configuration.
 *
 * @author stasha
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Configuration {

    /**
     * Configuration that will be used for running tests
     *
     * @return
     */
    Class<? extends TestConfig> configuration() default JerseyTestConfig.class;

    /**
     * Server configuration
     *
     * @return
     */
    Class<? extends ServerConfig> serverConfig() default JettyServerConfig.class;

    /**
     * DB configuration
     *
     * @return
     */
    Class<? extends DbConfig> dbConfig() default H2Config.class;

    /**
     * true/false if server should be started for the test
     *
     * @return
     */
    boolean runServer() default true;

    /**
     * true/fale if DB should start for the test
     *
     * @return
     */
    boolean runDb() default true;

    /**
     * Server base uri.
     *
     * @return
     */
    String baseUri() default TestConfig.BASE_URI;

    /**
     * Server httpPort
     *
     * @return
     */
    int httpPort() default TestConfig.HTTP_PORT;

    /**
     * When to start/stop server.
     *
     * @see StartServer
     * @return
     */
    StartServer startServer() default StartServer.PER_CLASS;

}
