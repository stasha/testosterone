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
```
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
```
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

    
    

