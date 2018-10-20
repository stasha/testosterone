package info.stasha.testosterone;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.NotSupportedException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PATCH;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.message.internal.MessageBodyProviderNotFoundException;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

/**
 * Class responsible for invoking test method.
 *
 * @author stasha
 */
public class InvokeRequest extends Statement {

	private static final String EXECUTION_ERROR_MESSAGE = "Test failed with message: ";

	private final FrameworkMethod method;
	private final JerseyRequestTest target;

	public InvokeRequest(FrameworkMethod method, Object target) {
		this.method = method;
		this.target = (JerseyRequestTest) target;
	}

	@Override
	public void evaluate() throws Throwable {

		Path p = method.getAnnotation(Path.class);
		String path = "";
		if (p != null) {
			path = p.value();
		}

		GET get = method.getAnnotation(GET.class);
		POST post = method.getAnnotation(POST.class);
		PUT put = method.getAnnotation(PUT.class);
		DELETE delete = method.getAnnotation(DELETE.class);
		PATCH patch = method.getAnnotation(PATCH.class);
		HEAD head = method.getAnnotation(HEAD.class);
		OPTIONS options = method.getAnnotation(OPTIONS.class);

		WebTarget webTarget = target.target(path);
		Response resp;

		if (post != null) {
			resp = webTarget.request().post(Entity.json(null));
		} else if (put != null) {
			resp = webTarget.request().put(Entity.json(null));
		} else if (delete != null) {
			resp = webTarget.request().delete();
		} else if (patch != null) {
			throw new NotSupportedException("patch is not supported");
		} else if (head != null) {
			resp = webTarget.request().head();
		} else if (options != null) {
			resp = webTarget.request().options();
		} else {
			resp = webTarget.request().get();
		}

		try {
			try {
				System.out.println("-----");
				System.out.println(resp);
				System.out.println("-----");
				if (resp.getStatus() > 400) {
					throw new Error(EXECUTION_ERROR_MESSAGE + " " + resp);
				}
			} catch (MessageBodyProviderNotFoundException ex) {
				throw new Error(EXECUTION_ERROR_MESSAGE + " " + ex.getMessage());
			}
		} catch (Error ex) {
			if (target.getMessages().isEmpty()) {
				System.out.println(ex.getMessage());
				throw new Error(ex.getMessage());
			}
		}
	}
}
