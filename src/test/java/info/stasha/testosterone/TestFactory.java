package info.stasha.testosterone;

import java.util.function.Supplier;

/**
 *
 * @author stasha
 */
public class TestFactory implements Supplier<MyService> {

	@Override
	public MyService get() {
		return new MyServiceImpl();
	}

	

	

}
