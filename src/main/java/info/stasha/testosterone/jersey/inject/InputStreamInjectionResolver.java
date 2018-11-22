package info.stasha.testosterone.jersey.inject;

import info.stasha.testosterone.TestInExecution;
import java.lang.reflect.AnnotatedElement;
import org.glassfish.hk2.api.Injectee;
import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.ServiceHandle;
import org.slf4j.LoggerFactory;
import info.stasha.testosterone.annotation.LoadFile;
import java.io.Closeable;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.inject.Provider;
import javax.ws.rs.core.Context;
import org.glassfish.jersey.server.CloseableService;
import org.slf4j.Logger;

/**
 * Input stream injection resolver.
 *
 * @author stasha
 */
public class InputStreamInjectionResolver implements InjectionResolver<LoadFile> {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputStreamInjectionResolver.class);

    @Context
    Provider<TestInExecution> test;

    @Context
    Provider<CloseableService> closeableService;

    @Override
    public Object resolve(Injectee injectee, ServiceHandle<?> handle) {
        TestInExecution t = test.get();
        if (!t.isTest() && !t.isRequest()) {
            return null;
        }
        AnnotatedElement el = injectee.getParent();
        LoadFile lf = el.getAnnotation(LoadFile.class);
        Type cls = injectee.getRequiredType();
        String path = lf.value();
        path = path.startsWith("/") ? path : "/" + path;
        Object out = null;
        if (injectee.getRequiredType() == InputStream.class) {
            out = this.getClass().getResourceAsStream(path);
            closeableService.get().add((Closeable) out);
        } else if (cls == String.class) {
            try {
                out = new String(Files.readAllBytes(Paths.get(this.getClass().getResource(path).getPath())));
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        } else {
            throw new IllegalStateException("@LoadFile can be used only on InputStream or String classes");
        }

        return out;
    }

    @Override
    public boolean isConstructorParameterIndicator() {
        return true;
    }

    @Override
    public boolean isMethodParameterIndicator() {
        return false;
    }

}
