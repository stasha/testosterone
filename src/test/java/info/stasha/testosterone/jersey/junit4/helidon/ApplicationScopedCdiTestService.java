/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.jersey.junit4.helidon;

import javax.enterprise.context.Dependent;

/**
 *
 * @author stasha
 */
@Dependent
public class ApplicationScopedCdiTestService {

    public static String MESSAGE = "message from application scoped cdi service";

    public String getMessage() {
        return MESSAGE;
    }

}
