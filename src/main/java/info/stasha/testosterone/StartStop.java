package info.stasha.testosterone;

/**
 * Start/Stop interface.
 *
 * @author stasha
 */
public interface StartStop {

    /**
     * Starts implementation
     *
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * Stops implementation
     *
     * @throws Exception
     */
    void stop() throws Exception;

    /**
     * Returns true/false if implementation is running.
     *
     * @return
     */
    boolean isRunning();
}
