/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.jersey.junit4.helidon;

import org.mockito.Mockito;

/**
 *
 * @author stasha
 */

public class DependentCdiTestServiceAlternative extends DependentCdiTestService {

    public static String MESSAGE = "message from dependent cdi service alternative";

    public String getMessage() {
        return MESSAGE;
    }
    
    public Object getOriginalObject(){
        return Mockito.spy(this);
    }

}
