# testosterone
Test your Jersey app without a compromise.

[![Build Status](https://travis-ci.org/stasha/testosterone.svg?branch=master)](https://travis-ci.org/stasha/testosterone)
[![CircleCI](https://circleci.com/gh/stasha/testosterone/tree/master.svg?style=svg)](https://circleci.com/gh/stasha/testosterone/tree/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/info.stasha/testosterone/badge.svg)](https://maven-badges.herokuapp.com/maven-central/info.stasha/testosterone)
[![Coverage Status](https://coveralls.io/repos/github/stasha/testosterone/badge.svg)](https://coveralls.io/github/stasha/testosterone)

### Install using maven:
```
<dependency>
  <groupId>info.stasha</groupId>
  <artifactId>testosterone</artifactId>
  <version>${testosterone.version}</version>
</dependency>
```
### Downlod from: 
```
https://repo.maven.apache.org/maven2/info/stasha/testosterone/
```

- simple to write code and tests 

- it's also architectural framework that simplifies writing tests from ground up: 
    1. [DAO - against in-memory or external DB](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/dao/TaskDaoTest.java)
    2. [Service - using mocks](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/service/TaskServiceTest.java)
    3. [Resource - using mocks](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/resource/TaskResourceTest.java)
    4. [Integration - against embedded server, in-memory or external DB](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointIntegrationTest.java)
    5. Integration - against any external environment including production

- supports:
    - all Jersey versions from 2.1 to 2.27
    - [JUnit4](https://github.com/stasha/testosterone/tree/master/src/main/java/info/stasha/testosterone/jersey/junit4), [JUnit5](https://github.com/stasha/testosterone/tree/master/src/main/java/info/stasha/testosterone/jersey/junit5) and [TestNG](https://github.com/stasha/testosterone/tree/master/src/main/java/info/stasha/testosterone/jersey/testng)
    - [all Jersey related stuff inside a test](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/InjectablesTest.java)
    - [mixed tests inside single test class, running on a server and clean unit test (clean unit test is annotated with @DontIntercept)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/InjectTestTest.java)
    - [REST endpoints and tests in a single class](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/HttpMethodsTest.java)
    - embedded [Jetty](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/servers/JettyServerConfig.java), [Tomcat](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/servers/TomcatServerConfig.java) and [Grizzly](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/servers/GrizzlyServerConfig.java] servers
    - [H2](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/H2Config.java), [Derby](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/DerbyConfig.java), [HSQLDB](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/HsqlDbConfig.java) in-memory databases
    - [Postgres](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/PostgresConfig.java) and [MySql](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/db/MySqlConfig.java) external databases
    - [servlets, filters, listeners and context params](https://github.com/stasha/testosterone/tree/master/src/test/java/info/stasha/testosterone/jersey/junit4/servlet)
    - local and global [mocks](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/MockInjectTest.java) and [spies](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/SpyInjectTest.java)
    - [simplified @Request annotation](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/RequestTest.java)
    - [@Request grouping using @Requests annotation](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/RequestTest.java)
    - [repeatable requests and request groups](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/RequestTest.java)
    - [URL path generation by specifying regex pattern in URL](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/RequestTest.java)
    - [integration testing on embedded](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointIntegrationTest.java) or [external server](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/request/ExternalBaseUriWithCustomJerseyPathTest.java)
    - injection of all [standard Jersey injectables](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/InjectablesTest.java) + additional Testosterone ones:
        - [Connection - injects DB connection](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/dao/TaskDaoTest.java)
        - [Testosterone - injects current executing test (useful for developing other components)](https://github.com/stasha/testosterone/blob/master/src/main/java/info/stasha/testosterone/jersey/inject/SpyInjectionResolver.java)
        - [DbConfig - injects DB configuration used in test (useful for obtaining Connection or DataSource)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/db/HsqlDbConfigTest.java)
    - additional annotations:
        - [@Mock - injects mocked object](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/MockInjectTest.java)
        - [@Spy - injects spied object](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/SpyInjectTest.java)
        - [@Value - injects value from resource (properties)](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/ValueInjectionTest.java)
        - [@LoadFile - injects String or InputStream](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/jersey/injectables/LoadFileTest.java)
    - clean separation between tested object and dependencies (mocks)
    - easy configuration of testing environment by providing custom implementations of ServerConfig, DbConfig or TestConfig
    - global tests configuration by system variable or ServiceLoader mechanism
    - single test configuration by @Configuration annotation
    - running same test methods with different configurations by simply subclassing test class and configuring it differently
    - on/off Server or DB per test class
    - freedom to extend any class (testosterone is an interface)
    - beforeServerStart, afterServerStart, beforeServerStop and afterServerStop callbacks
    - [@Dependencies](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/task/dao/TaskDaoTest.java) annotation for including other test classes as dependencies
    - [@Integration](https://github.com/stasha/testosterone/blob/master/src/test/java/info/stasha/testosterone/jersey/junit4/integration/test/TaskEndpointIntegrationTest.java) annotation for including other test classes that will be used in local integration test

### Minimum code required:

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

#### Test class configuration
```java
@Configuration(
  baseUri = "http://localhost/", 
  httpPort = 9998, 
  testConfig = JerseyTestConfig.class, 
  dbConfig = H2Config.class, 
  serverConfig = JettyServerConfig.class, 
  runServer = true, 
  runDb = true, 
  startServer = StartServer.PER_CLASS
)
public class TestNGTest implements Testosterone {
 
    @Test
    public void test(){
    }
}
```

    
    



