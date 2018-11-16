package info.stasha.testosterone;

import java.io.Closeable;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 *
 * @author stasha
 */
public class TestEventListenerImpl implements TestEventListener {

    String id;
    Object obj;

    public TestEventListenerImpl(String id, Object obj) {
        this.id = id;
        this.obj = obj;
    }

    @Override
    public void trigger() throws Exception {
        if (obj instanceof Callable) {
            ((Callable) obj).call();
        } else if (obj instanceof Closeable) {
            ((Closeable) obj).close();
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TestEventListenerImpl other = (TestEventListenerImpl) obj;
        return Objects.equals(this.id, other.id);
    }
}
