package info.stasha.testosterone.jersey.junit4.jersey.dynamicfeature;

import java.io.IOException;
import javax.ws.rs.GET;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author stasha
 */
@Provider
public class MyDynamicFeature implements DynamicFeature {

    @Override
    public void configure(ResourceInfo resourceInfo, FeatureContext context) {
        if (resourceInfo.getResourceClass().getName().equals(DynamicFeatureTest.class.getName() + "_")
                && resourceInfo.getResourceMethod().getAnnotation(GET.class) != null) {
            context.register(new ContainerResponseFilter() {
                @Override
                public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
                    responseContext.setStatus(Response.Status.BAD_GATEWAY.getStatusCode());
                }
            });
        }
    }

}
