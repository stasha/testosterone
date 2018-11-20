package info.stasha.testosterone.jersey.junit4.jersey.service;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;

/**
 * User class
 *
 * @author stasha
 */
public class User {

	@PathParam("state")
	private String state;

	@QueryParam("firstName")
	private String firstName = "First Name";

	@QueryParam("lastName")
	private String lastName = "Last Name";

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
