package info.stasha.testosterone.testng;

import java.lang.annotation.Annotation;
import org.testng.annotations.AfterClass;

/**
 * @BeforeClass annotation implementation.
 *
 * @author stasha
 */
public class AfterClassAnnotation implements AfterClass {

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public Class<? extends Annotation> annotationType() {
		return AfterClass.class;
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public boolean enabled() {
		return true;
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public String[] groups() {
		return new String[]{};
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public String[] dependsOnGroups() {
		return new String[]{};
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public String[] dependsOnMethods() {
		return new String[]{};
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public boolean alwaysRun() {
		return true;
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public boolean inheritGroups() {
		return true;
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public String description() {
		return "";
	}

	/**
	 * {@inheritDoc }
	 * 
	 * @return 
	 */
	@Override
	public long timeOut() {
		return 0;
	}

}
