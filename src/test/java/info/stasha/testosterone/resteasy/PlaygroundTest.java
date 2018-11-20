package info.stasha.testosterone.resteasy;

import com.google.inject.Binder;
import info.stasha.testosterone.ServerConfig;
import info.stasha.testosterone.resteasy.junit4.*;
import info.stasha.testosterone.junit4.TestosteroneRunner;
import javax.inject.Inject;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author stasha
 */
@RunWith(TestosteroneRunner.class)
public class PlaygroundTest implements Testosterone {

    @Inject
    ServerConfig service;

    @Override
    public void configure(Binder binder) {
//        binder.bind(Service.class).to(ServiceImpl.class);
    }

    @Test
    @Inject
    public void test(ServerConfig config) {
        System.out.println("test");
    }

}
