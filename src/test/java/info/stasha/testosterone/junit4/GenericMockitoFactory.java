package info.stasha.testosterone;

import org.glassfish.hk2.api.Factory;
import org.mockito.Mockito;

/**
 * Creates Jersey factory objects based on passed class.<br>
 *
 * NOTE: Making factories this way fails in Jersey 2.0, 2.1, 2.2, 2.3
 *
 * @author stasha
 */
public class GenericMockitoFactory {

	private enum Type {
		INSTANCE,
		SPY,
		MOCK
	}

	private static Factory newInstance(Class<?> obj, Type mode) {

		return new Factory() {
			@Override
			public Object provide() {
				try {
					switch (mode) {
						case SPY:
							return Mockito.spy(obj.cast(obj.newInstance()));
						case MOCK:
							return Mockito.mock(obj);
						default:
							return obj.newInstance();
					}
				} catch (InstantiationException | IllegalAccessException ex) {
					throw new RuntimeException(ex);
				}
			}

			@Override
			public void dispose(Object instance) {
				// do nothing
			}
		};
	}

	public static Factory get(Class<?> obj) {
		return newInstance(obj, Type.INSTANCE);
	}

	public static Factory spy(Class<?> obj) {
		return newInstance(obj, Type.SPY);
	}

	public static Factory mock(Class<?> obj) {
		return newInstance(obj, Type.MOCK);
	}

}
