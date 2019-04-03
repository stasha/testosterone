package info.stasha.testosterone.jersey.junit4.jersey.aop;

import info.stasha.testosterone.TestAnnotations;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import javax.ws.rs.GET;
import org.aopalliance.intercept.ConstructorInterceptor;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.glassfish.hk2.api.Filter;
import org.glassfish.hk2.api.InterceptionService;
import org.glassfish.hk2.utilities.BuilderHelper;
import org.jvnet.hk2.annotations.Service;

/**
 *
 * @author stasha
 */
@Service
public class MyMethodInterceptor implements InterceptionService {

    @Override
    public Filter getDescriptorFilter() {
        return BuilderHelper.allFilter();
    }

    @Override
    public List<MethodInterceptor> getMethodInterceptors(Method method) {
        if (method.getDeclaringClass().equals(MethodInterceptorTest.MyInterceptedResource.class)) {
            return Collections.singletonList(new MethodInterceptor() {
                @Override
                public Object invoke(MethodInvocation methodInvocation) throws Throwable {
                    if (methodInvocation.getMethod().getAnnotation(TestAnnotations.TEST.get(0)) == null) {
                        return "intercepted " + methodInvocation.proceed();
                    }
                    return null;
                }
            });
        }
        return null;
    }

    @Override
    public List<ConstructorInterceptor> getConstructorInterceptors(Constructor<?> c) {
        return null;
    }

}
