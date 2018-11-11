package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Request;
import com.mifmif.common.regex.Generex;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.annotation.RequestAnnotation;
import info.stasha.testosterone.annotation.Requests;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Class responsible for invoking test method.
 *
 * @author stasha
 */
public class TestExecutorImpl implements TestExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TestExecutorImpl.class);

    private static final String EXECUTION_ERROR_MESSAGE = "Test failed with message: ";

    private final Method method;
    private final Testosterone target;

    /**
     * Creates new InvokeTest instance.
     *
     * @param method
     * @param target
     */
    public TestExecutorImpl(Method method, Object target) {
        this.method = method;
        this.target = (Testosterone) target;
    }

    /**
     * Executes http request/s to JUnit tests.
     *
     * @throws Throwable
     */
    @Override
    public void execute() throws Throwable {

        Path p = method.getAnnotation(Path.class);
        String path = "";
        if (p != null) {
            path = p.value();
        }

        LOGGER.info("Running: {}:{} at path {}", target.getClass().getName(), method.getName(), path);

        GET get = method.getAnnotation(GET.class);
        POST post = method.getAnnotation(POST.class);
        PUT put = method.getAnnotation(PUT.class);
        DELETE delete = method.getAnnotation(DELETE.class);
        HEAD head = method.getAnnotation(HEAD.class);
        OPTIONS options = method.getAnnotation(OPTIONS.class);

        String requestMethod = HttpMethod.GET;

        Request requestAnnotatoin = method.getAnnotation(Request.class);
        Requests requestsAnnotation = method.getAnnotation(Requests.class);

        if (requestAnnotatoin != null && requestsAnnotation != null) {
            throw new IllegalStateException("@Requests and @Request annotations can't be used on same method: "
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
                if(requestAnnotatoin.url().isEmpty()){
                    RequestAnnotation ra = new RequestAnnotation(requestAnnotatoin);
                    ra.setUrl(path);
                    requestAnnotatoin = ra;
                }
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

                    Response resp;

                    Entity entity = Entity.json(null);

                    if (!r.entity().isEmpty()) {
                        try {
                            Field e = target.getClass().getField(r.entity());
                            entity = (Entity) e.get(target);
                        } catch (NoSuchFieldException fex) {
                            try {
                                Method en = target.getClass().getMethod(r.entity());
                                entity = (Entity) en.invoke(target, new Object[]{});
                            } catch (NoSuchMethodException mex) {
                                // do nothing
                            }
                        }
                    }

                    Invocation.Builder builder = webTarget.request();

                    for (String headerParam : requestAnnotatoin.headerParams()) {
                        String[] keyValue = headerParam.split(",");
                        builder = builder.header(keyValue[0].trim(), keyValue[1].trim());
                    }

                    switch (requestMethod) {
                        case HttpMethod.POST:
                            resp = builder.post(entity);
                            break;
                        case HttpMethod.PUT:
                            resp = builder.put(entity);
                            break;
                        case HttpMethod.DELETE:
                            resp = builder.delete();
                            break;
                        case HttpMethod.HEAD:
                            resp = builder.head();
                            break;
                        case HttpMethod.OPTIONS:
                            resp = builder.options();
                            break;
                        default:
                            resp = builder.get();
                    }

                    List<Object> params = new ArrayList<>();
                    for (Class<?> param : method.getParameterTypes()) {
                        if (param.equals(Response.class)) {
                            params.add(resp);
                        } else if (param.equals(Request.class)) {
                            params.add(r);
                        } else if (param.equals(WebTarget.class)) {
                            params.add(webTarget);
                        }
                    }

                    if (params.size() > 0) {
                        Utils.invokeOriginalMethod(method, target, params.toArray());
                    }

                    // asserting response status if it was set on request
                    String s = Arrays.toString(r.expectedStatus()).replace(",", " or ");
                    List<Integer> list = Arrays.stream(r.expectedStatus()).boxed().collect(Collectors.toList());
                    if (r.expectedStatus().length > 0 && !list.contains(resp.getStatus())) {
                        target.getServerConfig().getExceptions()
                                .add(new AssertionError("Response status code should be " + s + ". Expecting " + s + " but was [" + resp.getStatus() + "]"));
                    }

                }
            }
        }
    }
}
