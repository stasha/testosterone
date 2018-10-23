/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.weblistener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * WebListener class
 *
 * @author stasha
 */
@javax.servlet.annotation.WebListener
public class WebListener implements ServletContextListener {

	public WebListener() {
		System.out.println("web listener");
	}

	@Override
	public void contextInitialized(ServletContextEvent event) {
		System.out.println("Servlet context initialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		System.out.println("Servlet context destroyed");
	}
}
