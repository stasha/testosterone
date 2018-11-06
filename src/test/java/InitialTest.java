
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import org.junit.Test;

/**
 *
 * @author stasha
 */
public class InitialTest {

	@Test
	public void initialTest() {
		ByteBuddyAgent.install();
		new ByteBuddy();
		System.out.println("Initial Test");
	}
}
