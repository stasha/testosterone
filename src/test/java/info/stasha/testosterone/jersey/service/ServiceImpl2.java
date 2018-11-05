package info.stasha.testosterone.jersey.service;

/**
 * Service implementation
 *
 * @author stasha
 */
public class ServiceImpl2 implements Service2 {

	private User user;

	@Override
	public String getText() {
		return RESPONSE_TEXT;
	}

	@Override
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public void throwIllegalStateException() {
		throw new IllegalStateException();
	}

}