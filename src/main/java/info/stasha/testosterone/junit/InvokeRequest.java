package info.stasha.testosterone.junit;

import info.stasha.testosterone.annotation.Request;
import com.mifmif.common.regex.Generex;
import info.stasha.testosterone.jersey.JerseyRequestTest;
import info.stasha.testosterone.annotation.RequestAnnotation;
import info.stasha.testosterone.annotation.Requests;
import java.util.Arrays;
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
import org.eclipse.jetty.http.HttpMethod;
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

		HttpMethod requestMethod = HttpMethod.GET;

		Request requestAnnotatoin = method.getAnnotation(Request.class);
		Requests requestsAnnotation = method.getAnnotation(Requests.class);

		if (requestAnnotatoin != null && requestsAnnotation != null) {
			throw new IllegalStateException("You can use @Requests and @Request annotations on the same method: "
					+ method.getName());
		}

		int requestsRepeat = (requestsAnnotation == null || requestsAnnotation.repeat() == 0) ? 1 : requestsAnnotation.repeat();

		for (int i = 0; i < requestsRepeat; ++i) {

			Request[] reqs;

			if (requestsAnnotation != null) {
				reqs = requestsAnnotation.requests();
				if (reqs.length == 0) {
					throw new IllegalStateException("@Requests annotation may not be empty.");
				}
			} else if (requestAnnotatoin != null) {
				reqs = new Request[]{requestAnnotatoin};
			} else {
				reqs = new Request[]{new RequestAnnotation(path)};
			}

			for (Request r : reqs) {

				if (Arrays.stream(r.excludeFromRepeat()).anyMatch(((Integer) (i + 1))::equals)) {
					continue;
				}

				requestAnnotatoin = r;

				if (requestAnnotatoin.method() != null) {
					requestMethod = requestAnnotatoin.method();
				} else {
					if (post != null) {
						requestMethod = HttpMethod.POST;
					} else if (put != null) {
						requestMethod = HttpMethod.PUT;
					} else if (delete != null) {
						requestMethod = HttpMethod.DELETE;
					} else if (patch != null) {
						throw new NotSupportedException("patch is not supported");
					} else if (head != null) {
						requestMethod = HttpMethod.HEAD;
					} else if (options != null) {
						requestMethod = HttpMethod.OPTIONS;
					} else {
						requestMethod = HttpMethod.GET;
					}
				}

				int requestRepeat = requestAnnotatoin.repeat() == 0 ? 1 : requestAnnotatoin.repeat();
				String normalizedUrl = requestAnnotatoin.url();
				normalizedUrl = normalizedUrl
						.replaceAll("\\?", "\\\\\\?")
						.replaceAll("\\&", "\\\\\\&")
						.replaceAll("\\.", "\\\\\\.")
						.replaceAll("\\*", "\\\\\\*");
				Generex generex = new Generex(normalizedUrl);
				//generex.random();
				for (int m = 0; m < requestRepeat; ++m) {
					String generexUrl = generex.random();

					String[] url = generexUrl.split("\\?");

					WebTarget webTarget = target.target(url[0]);

					if (url.length > 1 && !url[1].isEmpty()) {

						String[] query = url[1].split("&");

						for (String params : query) {
							String[] param = params.split("=");
							String value;
							if (param.length == 1) {
								value = "";
							} else {
								value = param[1];
							}
							webTarget = webTarget.queryParam(param[0], value);
						}
					}

					for (String headerParam : requestAnnotatoin.headerParams()) {
					}

					Response resp;

					switch (requestMethod) {
						case POST:
							resp = webTarget.request().post(Entity.json(null));
							break;
						case PUT:
							resp = webTarget.request().put(Entity.json(null));
							break;
						case DELETE:
							resp = webTarget.request().delete();
							break;
						case HEAD:
							resp = webTarget.request().head();
							break;
						case OPTIONS:
							resp = webTarget.request().options();
							break;
						default:
							resp = webTarget.request().get();
					}

					for (Class<?> param : method.getMethod().getParameterTypes()) {
						if (param.equals(Response.class)) {
							method.invokeExplosively(target, resp);
						}
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
		}
	}
}
