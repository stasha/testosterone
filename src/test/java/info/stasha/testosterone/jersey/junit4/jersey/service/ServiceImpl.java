package info.stasha.testosterone.jersey.junit4.jersey.service;

/**
 * Service implementation
 *
 * @author stasha
 */
public class ServiceImpl implements Service, MyService, GreetService {

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
