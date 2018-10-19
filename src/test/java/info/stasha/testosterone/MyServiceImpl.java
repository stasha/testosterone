package info.stasha.testosterone;


/**
 *
 * @author stasha
 */
public class MyServiceImpl implements MyService {

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

}
