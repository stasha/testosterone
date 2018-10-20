package info.stasha.testosterone.service;

/**
 * Simple service
 *
 * @author stasha
 */
public interface Service {

	public static final String RESPONSE_TEXT = "Hello from MyService";

	String getText();

	User getUser();
}
