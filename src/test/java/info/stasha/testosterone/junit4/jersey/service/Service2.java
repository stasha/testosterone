package info.stasha.testosterone.jersey.service;

/**
 * Simple service
 *
 * @author stasha
 */
public interface Service2 {

	public static final String RESPONSE_TEXT = "Hello from MyService";

	String getText();

	User getUser();

	void throwIllegalStateException();
}
