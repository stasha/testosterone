/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.helidon;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

/**
 *
 * @author stasha
 */
@Dependent
public class DependentCdiTestService {

    public static String MESSAGE = "message from dependent cdi service";

    @Inject
    ApplicationScopedCdiTestService service;

    public String getMessage() {
        return MESSAGE;
    }

    public String getApplicationMessage() {
        return service.getMessage();
    }

}
