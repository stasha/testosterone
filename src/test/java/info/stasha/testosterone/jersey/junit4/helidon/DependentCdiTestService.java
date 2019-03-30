/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.jersey.junit4.helidon;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author stasha
 */
@Dependent
public class DependentCdiTestService {

    public static String MESSAGE = "message from dependent cdi service";

    @PostConstruct
    public void init() {
        System.out.println(this + " has initialized");
    }

    @PreDestroy
    public void destroy() {
        System.out.println(this + " has destroyed");
    }

    @Inject
    ApplicationScopedCdiTestService service;

    public String getMessage() {
        return MESSAGE;
    }

    public String getApplicationMessage() {
        return service.getMessage();
    }

}
