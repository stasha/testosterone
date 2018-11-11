package info.stasha.testosterone.junit4.integration.test.dao;

import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import info.stasha.testosterone.junit4.integration.app.task.Task;
import info.stasha.testosterone.junit4.integration.app.task.dao.TaskDao;
import info.stasha.testosterone.junit4.integration.app.task.dao.TaskDaoFactory;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.ws.rs.core.Context;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class TaskDaoTest implements Testosterone {

    @Context
    Connection conn;

    @Context
    private TaskDao taskDao;

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(TaskDaoFactory.class).to(TaskDao.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Before
    public void setUp() throws Exception {
        String create = "CREATE TABLE tasks (\n"
                + "  id BIGINT(11) NOT NULL auto_increment PRIMARY KEY,\n"
                + "  title VARCHAR(56) NOT NULL,\n"
                + "  description VARCHAR(56) NOT NULL,\n"
                + "  done BOOLEAN NOT NULL,\n"
                + "  created_at DATETIME,\n"
                + "  updated_at DATETIME\n"
                + "  )";

        conn.prepareStatement(create).executeUpdate();

        taskDao.createTask(new Task("Create Task Test1", "Testing TaskDao1", Boolean.FALSE));
        taskDao.createTask(new Task("Create Task Test2", "Testing TaskDao2", Boolean.FALSE));
        taskDao.createTask(new Task("Create Task Test3", "Testing TaskDao3", Boolean.FALSE));

        assertEquals("Task list should contain 1 task", 3, taskDao.getAllTasks().size());
    }

    @After
    public void tearDown() throws Exception {
        conn.prepareStatement("drop table tasks").executeUpdate();
    }

    @Test
    public void readAllTasks() throws Exception {
        taskDao.createTask(new Task("Create Task Test 4", "Testing TaskDao 2", Boolean.FALSE));

        List<Task> tasks = taskDao.getAllTasks();
        assertEquals("Task list should contain 1 task", 4, tasks.size());
    }

    @Test
    public void readTask() throws Exception {
        Task dbtask = taskDao.getTask(new Task(3L));
        assertNotNull("Returned task from db should be not null", dbtask);
    }

    @Test
    public void updateTask() throws Exception {
        this.taskDao.updateTask(new Task(2L).setTitle("Update Task Test").setDescription("Updating testing TaskDao").setDone(Boolean.TRUE));

        Task dbtask = taskDao.getTask(new Task(2L));
        assertNotNull("Returned task from db should be not null", dbtask);
        assertEquals("Title should be updated", "Update Task Test", dbtask.getTitle());
        assertEquals("Title should be updated", "Updating testing TaskDao", dbtask.getDescription());
        assertEquals("Done should be true", true, dbtask.getDone());
    }

    @Test
    public void deleteTask() throws Exception {
        this.taskDao.deleteTask(new Task(1L));

        Task dbtask = taskDao.getTask(new Task(1L));

        assertNull("Returned task should be null", dbtask);
    }

    @Test(expected = NullPointerException.class)
    public void createSqlException() throws SQLException {
        this.taskDao.createTask(new Task());
    }

}
