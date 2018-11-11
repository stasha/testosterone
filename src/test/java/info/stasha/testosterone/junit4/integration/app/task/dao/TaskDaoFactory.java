package info.stasha.testosterone.junit4.integration.app.task.dao;

import javax.ws.rs.core.Context;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.api.ServiceLocator;

/**
 *
 * @author stasha
 */
public class TaskDaoFactory implements Factory<TaskDao> {

	@Context
	private ServiceLocator locator;

	@Override
	public TaskDao provide() {
		return locator.createAndInitialize(TaskDaoImpl.class);
	}

	@Override
	public void dispose(TaskDao instance) {

	}

}
