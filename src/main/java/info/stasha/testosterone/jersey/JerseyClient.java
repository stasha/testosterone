package info.stasha.testosterone.jersey;

import info.stasha.testosterone.StartStop;
import info.stasha.testosterone.TestConfig;
import java.util.concurrent.atomic.AtomicReference;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import org.glassfish.jersey.client.ClientConfig;

/**
 * Jersey Client
 *
 * @author stasha
 */
public class JerseyClient implements StartStop {

    private final TestConfig config;
    private final AtomicReference<Client> client = new AtomicReference<>(null);

    public JerseyClient(TestConfig config) {
        this.config = config;
    }

    public Client client() {
        if (client.get() == null) {
            client.getAndSet(getClient());
        }
        return client.get();
    }

    protected Client getClient() {
        ClientConfig clientConfig = new ClientConfig();
        return ClientBuilder.newClient(clientConfig);
    }

    public WebTarget target() {
        try {
            return client().target(config.getBaseUri());
        } catch (Exception ex) {
            throw ex;
        }
    }

    /**
     * Closes client.
     *
     * @param clients
     */
    protected void closeClient(final Client... clients) {
        if (clients == null || clients.length == 0) {
            return;
        }

        for (Client c : clients) {
            if (c == null) {
                continue;
            }
            try {
                c.close();
                client.getAndSet(null);
            } catch (Throwable ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }

        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        Client old = client.getAndSet(getClient());
        closeClient(old);
    }

    /**
     * {@inheritDoc }
     *
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        closeClient(client.get());
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public boolean isRunning() {
        throw new UnsupportedOperationException("Not supported");
    }

}
