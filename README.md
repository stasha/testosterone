![Testosterona cartoon](https://github.com/stasha/testosterone/blob/master/testosterona.png)


# testosterone
is architectural testing framework designed to simplify tests writing and Jersey based applications testing.<br />
Also, it simplifies writing "free-form" or "cowboy" tests.

[![Build Status](https://travis-ci.org/stasha/testosterone.svg?branch=master)](https://travis-ci.org/stasha/testosterone)
[![CircleCI](https://circleci.com/gh/stasha/testosterone/tree/master.svg?style=svg)](https://circleci.com/gh/stasha/testosterone/tree/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/info.stasha/testosterone/badge.svg)](https://maven-badges.herokuapp.com/maven-central/info.stasha/testosterone)
[![Coverage Status](https://coveralls.io/repos/github/stasha/testosterone/badge.svg)](https://coveralls.io/github/stasha/testosterone)



Testosterone is built around idea where components are tested from ground up ending with joined tests into integration.
1. [Test DAO - against in-memory or external DB](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/dao/TaskDaoTest.java)
2. [Test service - using mocks](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/service/TaskServiceTest.java)
3. [Test resource - using mocks](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/resource/TaskResourceTest.java)
4. [Integration test - against embedded server, in-memory or external DB](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointIntegrationTest.java)
5. [Integration test - against any external environment](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointExternalIntegrationTest.java)

Take a look at [example](https://github.com/stasha/testosterone/tree/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration) code.


### Testosterone supports
- **ALL Jersey versions from 2.1 to 2.27**
###
- testing frameworks 
    - [JUnit4](https://github.com/stasha/testosterone/tree/master/src/main/java/info/stasha/testosterone/jersey/junit4) 
    - [JUnit5](https://github.com/stasha/testosterone/tree/master/src/main/java/info/stasha/testosterone/jersey/junit5)
    - [TestNG](https://github.com/stasha/testosterone/tree/master/src/main/java/info/stasha/testosterone/jersey/testng)
- embedded servers 
    - [Jetty](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/servers/JettyServerConfig.java)
    - [Tomcat](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/servers/TomcatServerConfig.java)
    - [Grizzly](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/servers/GrizzlyServerConfig.java) 
- in-memory databases 
    - [H2](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/H2Config.java)
    - [Derby](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/DerbyConfig.java)
    - [HSQLDB](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/HsqlDbConfig.java)
- external databases 
    - [Postgres](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/PostgresConfig.java) 
    - [MySql](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/MySqlConfig.java) 
- servlet components in Jetty and Tomcat
    - [Servlets](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/servlet/ServletJettyTest.java)
    - [Filters](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/servlet/servletfilter/ServletFilterJettyTest.java)
    - [Listeners](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/servlet/servletlistener/ServletListenerJettyTest.java)
    - [Context Params](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/servlet/servletcontextparams/ServletContextParamsJettyTest.java)
- injectables
    - default
        - [all provided by Jersey](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/InjectablesTest.java)
    - additional
        - [Connection - injects DB connection](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/dao/TaskDaoTest.java)
        - [Testosterone - injects current executing test (useful for developing other components)](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/jersey/inject/SpyInjectionResolver.java)
        - [DbConfig - injects DB configuration used in test (useful for obtaining Connection or DataSource)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/db/HsqlDbConfigTest.java)
- additional annotations
    - [@Mock - injects mocked object](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/MockInjectTest.java)
    - [@Spy - injects spied object](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/SpyInjectTest.java)
    - [@Value - injects value from resource (properties)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/ValueInjectionTest.java)
    - [@LoadFile - injects String or InputStream](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/LoadFileTest.java) 
    - [@Request - sends request to specified URL](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/RequestTest.java)
    - [@Requests - groups @Request annotations](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/RequestTest.java)
    - [@Dependencies - includes other test classes as dependencies](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/dao/TaskDaoTest.java)
    - [@Integration - includes other test classes into local integration test](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointIntegrationTest.java)
- life-cycle callbacks
    - [beforeServerStart](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/lifecycle/TestLifeCyclePerClassTest.java)
    - [afterServerStart](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/lifecycle/TestLifeCyclePerClassTest.java)
    - [beforeServerStop](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/lifecycle/TestLifeCyclePerClassTest.java)
    - [afterServerStop](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/lifecycle/TestLifeCyclePerClassTest.java)
- register custom ServerConfig, DbConfig and TestConfig configurations
    - globally
        - by setting system properties
            - testosterone.default.server.config
            - testosterone.default.db.config
            - testosterone.default.test.config
        - by using ServiceLoader mechanism
            - /META-INF/services/info.stasha.testosterone.ServerConfig
            - /META-INF/services/info.stasha.testosterone.DbConfig
            - /META-INF/services/info.stasha.testosterone.TestConfig
    - locally
        - [by annotating test class with @Configuration annotation](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/user/dao/UserDaoTest.java)
- clean code configuration separation with standardized methods
    - [configure(...)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/user/dao/UserDaoTest.java)
    - [configureMocks(...)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/user/dao/UserDaoTest.java)
- integration testing
    - [locally on embedded server with in-memory or external DB](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointIntegrationTest.java)
    - [remote](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointExternalIntegrationTest.java)

### Install using maven
```xml
<dependency>
    <groupId>info.stasha</groupId>
    <artifactId>testosterone</artifactId>
    <version>${testosterone.version}</version>
</dependency>
```

### Downlod from
```
https://repo.maven.apache.org/maven2/info/stasha/testosterone/
```

### Minimum dependencies for running tests
```xml
<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-grizzly2-servlet</artifactId>
    <version>2.27</version>
</dependency>

<dependency>
    <groupId>org.glassfish.jersey.containers</groupId>
    <artifactId>jersey-container-grizzly2-http</artifactId>
    <version>2.27</version>
</dependency>

<!-- Required for Jersey version 2.26 and higher. For lower versions
this dependency should be removed because it makes conflicts -->
<dependency>
    <groupId>org.glassfish.jersey.inject</groupId>
    <artifactId>jersey-hk2</artifactId>
    <version>2.27</version>
</dependency>

<dependency>
    <groupId>info.stasha</groupId>
    <artifactId>testosterone</artifactId>
    <version>3.0.0</version>
    <scope>test</scope>
</dependency>

<dependency>
    <groupId>junit</groupId>
    <artifactId>junit</artifactId>
    <version>4.12</version>
    <scope>test</scope>
</dependency>
```


### Minimum code required

#### JUnit4
```java
import info.stasha.testosterone.jersey.junit4.Testosterone;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TestosteroneRunner.class)
public class JUnit4Test implements Testosterone {
    
    @Test
    public void test(){
    }
}
```

#### JUnit5
```java
import info.stasha.testosterone.jersey.junit5.Testosterone;
import org.junit.jupiter.api.Test;

public class JUnit5Test implements Testosterone {
 
    @Test
    public void test(){
    }
}
```

#### TestNG
```java
import info.stasha.testosterone.jersey.testng.Testosterone;
import org.testng.annotations.Test;

public class TestNGTest implements Testosterone {
 
    @Test
    public void test(){
    }
}
```

#### Test class configuration defaults
```java
@Configuration(
  baseUri = "http://localhost/", 
  httpPort = 9998, 
  testConfig = JerseyTestConfig.class, 
  dbConfig = H2Config.class, 
  serverConfig = JettyServerConfig.class, 
  runServer = true, 
  runDb = false, 
  startServer = StartServer.PER_CLASS
)
public class TestNGTest implements Testosterone {
 
    @Test
    public void test(){
    }
}
```
## Examples
#### DAO test
```java
@RunWith(TestosteroneRunner.class)
@Dependencies(
        UserDaoTest.class
)
@Configuration(startServer = StartServer.PER_TEST_METHOD, runDb = true)
public class TaskDaoTest implements Testosterone {

    private final String createTasksTable = "CREATE TABLE tasks (\n"
            + "  id BIGINT(11) NOT NULL auto_increment PRIMARY KEY,\n"
            + "  title VARCHAR(56) NOT NULL,\n"
            + "  description VARCHAR(56) NOT NULL,\n"
            + "  done BOOLEAN NOT NULL,\n"
            + "  users_user_id BIGINT(11) NOT NULL,\n"
            + "  created_at DATETIME,\n"
            + "  updated_at DATETIME,\n"
            + "  FOREIGN KEY (users_user_id) REFERENCES users(id)\n"
            + "  )";

    @Context
    Connection conn;

    @Context
    private TaskDao taskDao;

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(TaskDaoFactory.class).to(TaskDao.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Override
    public void configure(DbConfig config) {
        config.add("createTasksTable", createTasksTable);
    }

    @Override
    public void configureMocks(DbConfig config) {
        config.add("addMockTasks", "insert into tasks (title, description, done, users_user_id) values "
                + "('Create Task Test1', 'Testing TaskDao1', false, 1),"
                + "('Create Task Test2', 'Testing TaskDao2', true, 2),"
                + "('Create Task Test3', 'Testing TaskDao3', false, 3)");
    }

    @Before
    public void setUp() throws Exception {
        assertEquals("Task list should contain 3 tasks", 3, taskDao.getAllTasks().size());
    }

    @After
    public void tearDown() throws Exception {
        conn.prepareStatement("drop table tasks").executeUpdate();
    }

    @Test
    public void readAllTasks() throws Exception {
        taskDao.createTask(new Task("Create Task Test 4", "Testing TaskDao 2", Boolean.FALSE, new User(1L)));

        List<Task> tasks = taskDao.getAllTasks();
        assertEquals("Task list should contain 4 tasks", 4, tasks.size());
    }

    @Test
    public void readTask() throws Exception {
        Task dbtask = taskDao.getTask(new Task(3L));
        assertNotNull("Returned task from db should not be null", dbtask);
    }

    @Test
    public void updateTask() throws Exception {
        this.taskDao.updateTask(new Task(2L).setTitle("Update Task Test").setDescription("Updating testing TaskDao").setDone(Boolean.TRUE));

        Task dbtask = taskDao.getTask(new Task(2L));
        assertNotNull("Returned task sould not be null", dbtask);
        assertEquals("Title should be updated", "Update Task Test", dbtask.getTitle());
        assertEquals("Description should be updated", "Updating testing TaskDao", dbtask.getDescription());
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
```
#### Service test
```java
@RunWith(TestosteroneRunner.class)
public class TaskServiceTest implements Testosterone {

    @Context
    private TaskService taskService;

    @Context
    private TaskDao taskDao;

    private final Task task = new Task("New Task", "New Task Description", false).setId(1L);

    @Override
    public void configure(AbstractBinder binder) {
        binder.bindFactory(TaskServiceFactory.class).to(TaskService.class).in(RequestScoped.class).proxy(true).proxyForSameScope(false);
    }

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bindFactory(FactoryUtils.<TaskDao>mock(TaskDaoFactory.class)).to(TaskDao.class).in(Singleton.class);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createTest() throws Exception {
        taskService.createTask(new Task(1L));
    }

    @Test(expected = NullPointerException.class)
    public void createTest2() throws Exception {
        taskService.createTask(null);
    }

    @Test
    public void createTest3() throws Exception {
        taskService.createTask(task.setId(null));
        verify(taskDao, 1).createTask(task.setId(null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void readTest() throws Exception {
        taskService.getTask(new Task());
    }

    @Test(expected = NullPointerException.class)
    public void readTest2() throws Exception {
        taskService.getTask(null);
    }

    @Test
    public void readTest3() throws Exception {
        taskService.getTask(task);
        Mockito.verify(taskDao, times(1)).getTask(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void updateTest() throws Exception {
        taskService.updateTask(new Task());
    }

    @Test(expected = NullPointerException.class)
    public void updateTest2() throws Exception {
        taskService.updateTask(null);
    }

    @Test
    public void updateTest3() throws Exception {
        taskService.updateTask(task);
        Mockito.verify(taskDao, times(1)).updateTask(task);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteTest() throws Exception {
        taskService.deleteTask(new Task());
    }

    @Test(expected = NullPointerException.class)
    public void deleteTest2() throws Exception {
        taskService.deleteTask(null);
    }

    @Test
    public void deleteTest3() throws Exception {
        taskService.deleteTask(task);
        Mockito.verify(taskDao, times(1)).deleteTask(task);
    }

}
```
#### Resource test
```java
@RunWith(TestosteroneRunner.class)
public class TaskResourceTest implements Testosterone {

    protected final Task task = new Task(3L, "Title", "Description", false);
    protected final Task createTask = new Task("Title", "Description", false);
    public Entity taskEntity = Entity.json(task);
    public Entity createTaskEntity = Entity.json(createTask);

    private final TaskResource taskResource = Mockito.spy(new TaskResource());

    @Context
    protected TaskService taskService;

    @Override
    public void configure(ResourceConfig config) {
        config.register(taskResource);
    }

    @Override
    public void configureMocks(AbstractBinder binder) {
        binder.bindFactory(FactoryUtils.<TaskService>mock(TaskServiceFactory.class)).to(TaskService.class).in(Singleton.class);
    }

    public <T> T verify(T mock, int invocations) {
        return Mockito.verify(mock, times(invocations));
    }

    @Test
    @Request(url = "task", method = GET, expectedStatus = {200, 204})
    public void getAllTasks(Response resp) throws Exception {
        verify(taskResource, 1).getAllTasks();
        verify(taskService, 1).getAllTasks();
    }

    @Test
    @Request(url = "task", method = POST, entity = "taskEntity", expectedStatus = {200, 204})
    public void createTask(Response resp) throws Exception {
        verify(taskResource, 1).createTask(Matchers.refEq(task));
        verify(taskService, 1).createTask(Matchers.refEq(task));
    }

    @Test
    @Request(url = "task/1", method = GET, expectedStatus = {200, 204})
    public void getTask(Response resp) throws Exception {
        verify(taskResource, 1).getTask(any(Task.class));
        verify(taskService, 1).getTask(any(Task.class));
    }

    @Test
    @Request(url = "task/1", method = PUT, entity = "taskEntity", expectedStatus = {200, 204})
    public void updateTask(Response resp) throws Exception {
        verify(taskResource, 1).updateTask(Matchers.refEq(task));
        verify(taskService, 1).updateTask(Matchers.refEq(task));
    }

    @Test
    @Request(url = "task/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteTask(Response resp) throws Exception {
        verify(taskResource, 1).deleteTask(any(Task.class));
        verify(taskService, 1).deleteTask(any(Task.class));
    }

}
```
#### Local integration test
```java
@Integration({
    TaskResourceTest.class,
    TaskServiceTest.class,
    TaskDaoTest.class,
    UserResourceTest.class,
    UserServiceTest.class,
    UserDaoTest.class
})
@RunWith(TestosteroneRunner.class)
@Configuration(runDb = true)
public class TaskEndpointIntegrationTest implements Testosterone {

    protected TaskResource taskResource;
    protected final Task task = new Task(3L, "Title", "Description", false, new User(2L));
    protected final Task createTask = new Task("Title", "Description", false, new User(1L));
    public Entity taskEntity = Entity.json(task);
    public Entity createTaskEntity = Entity.json(createTask);

    @Before
    public void setUp() throws SQLException {
        target("user").request().post(Entity.json(new User("Jon", "Doe", 23)));
    }

    @Test
    @Request(url = "task", method = GET, expectedStatus = {200, 204})
    public void getAllTasks(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task", method = POST, entity = "taskEntity", expectedStatus = {500})
    public void failCreateTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task", method = POST, entity = "createTaskEntity", expectedStatus = {200, 204})
    public void createTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task/1", method = GET, expectedStatus = {200, 204})
    public void getTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task/1", method = PUT, entity = "taskEntity", expectedStatus = {200, 204})
    public void updateTask(Response resp) throws Exception {
    }

    @Test
    @Request(url = "task/1", method = DELETE, expectedStatus = {200, 204})
    public void deleteTask(Response resp) throws Exception {
    }

}
```
#### External/remote integration test
```java
// Just extend local integration test and change configuration
@Configuration(baseUri = "http://myapp.com/", httpPort = 80, runServer = false)
public class TaskEndpointExternalIntegrationTest extends TaskEndpointIntegrationTest {

}
