package info.stasha.testosterone.jersey.junit4.servlet.servletlistener;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * All servlet listener
 *
 * @author stasha
 */
public class ServletListener implements
		ServletContextListener,
		ServletContextAttributeListener,
		ServletRequestListener,
		ServletRequestAttributeListener,
		HttpSessionListener,
		HttpSessionAttributeListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		System.out.println("context initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		System.out.println("context destroyed");
	}

	@Override
	public void attributeAdded(ServletContextAttributeEvent event) {
		System.out.println("context attribute added");
	}

	@Override
	public void attributeRemoved(ServletContextAttributeEvent event) {
		System.out.println("context attribute removed");
	}

	@Override
	public void attributeReplaced(ServletContextAttributeEvent event) {
		System.out.println("context attribute replaced");
	}

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		System.out.println("request destroyed");
	}

	@Override
	public void requestInitialized(ServletRequestEvent sre) {
		System.out.println("request initialized");
	}

	@Override
	public void attributeAdded(ServletRequestAttributeEvent srae) {
		System.out.println("request attribute added");
	}

	@Override
	public void attributeRemoved(ServletRequestAttributeEvent srae) {
		System.out.println("request attribute removed");
	}

	@Override
	public void attributeReplaced(ServletRequestAttributeEvent srae) {
		System.out.println("request attribute replaced");
	}

	@Override
	public void sessionCreated(HttpSessionEvent se) {
		System.out.println("session created");
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		System.out.println("session destroyed");
	}

	@Override
	public void attributeAdded(HttpSessionBindingEvent event) {
		System.out.println("session attribute added");
	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent event) {
		System.out.println("session attribute removed");
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent event) {
		System.out.println("session attribute replaced");
	}

}
