package info.stasha.testosterone.jersey.junit4.jersey.service;

/**
 * Simple service
 *
 * @author stasha
 */
public interface Service {

	public static final String RESPONSE_TEXT = "Hello from MyService";

	String getText();

	User getUser();

	void throwIllegalStateException();
}
