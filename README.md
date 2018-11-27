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
    1. DAO - against in-memory or external DB
    2. Service - using mocks
    3. Resource - using mocks
    4. Integration - against embedded server, in-memory or external DB
    5. Integration - against any external environment including production

- supports:
    - all Jersey versions from 2.1 to 2.27
    - JUnit4, JUnit5 and TestNG
    - all Jersey related stuff inside a test
    - mixed tests inside test class (running on a server or clean unit test)
    - REST endpoints and tests in a single class
    - embedded Jetty, Tomcat and Grizzly servers
    - H2, Derby, HSQLDB in-memory databases
    - Postgres and MySql external databases
    - servlets, filters, listeners and context params
    - local and global mocks
    - simplified @Request annotation
    - @Request grouping using @Requests annotation
    - repeatable requests and request groups
    - URL path generation by specifying regex pattern in URL
    - integration testing on embedded or external server
    - injection of all standard Jersey injectables + additional Testosterone ones:
        - Connection - injects DB connection
        - Testosterone - injects current executing test (useful for developing other components)
        - DbConfig - injects DB configuration used in test (useful for obtaining Connection or DataSource)
    - additional annotations:
        - @Mock - injects mocked object
        - @Spy - injects spied object
        - @Value - injects value from resource (properties)
        - @LoadFile - injects String or InputStream
    - clean separation between tested object and dependencies (mocks)
    - easy configuration of testing environment by providing custom implementations of ServerConfig, DbConfig or TestConfig
    - global tests configuration by system variable or ServiceLoader mechanism
    - single test configuration by @Configuration annotation
    - running same test methods with different configurations by simply subclassing test class and configuring it differently
    - on/off Server or DB per test class
    - freedom to extend any class (testosterone is an interface)
    - beforeServerStart, afterServerStart, beforeServerStop and afterServerStop callbacks
    - @Dependencies annotation for including other test classes as dependencies
    - @Integration annotation for including other test classes that will be used in local integration test

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


    
    



