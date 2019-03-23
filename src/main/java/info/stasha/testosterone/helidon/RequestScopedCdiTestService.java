/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.helidon;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

/**
 *
 * @author stasha
 */
@RequestScoped
public class RequestScopedCdiTestService {

    DependentCdiTestService tc;

    @Context
    private UriInfo uriInfo;

    @Inject
    public void setTestClass(DependentCdiTestService tc) {
        this.tc = tc;
    }

    public String getDependentMessage() {
        return this.tc.getMessage();
    }

    public String getApplicationScopedMessage() {
        return this.tc.getApplicationMessage();
    }

    public UriInfo getUriInfo() {
        return uriInfo;
    }
}
