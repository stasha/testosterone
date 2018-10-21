package info.stasha.testosterone.service;

/**
 * Service implementation
 *
 * @author stasha
 */
public class ServiceImpl implements Service {

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
