package info.stasha.testosterone;

import info.stasha.testosterone.annotation.Request;
import com.mifmif.common.regex.Generex;
import info.stasha.testosterone.jersey.Testosterone;
import info.stasha.testosterone.annotation.RequestAnnotation;
import info.stasha.testosterone.annotation.Requests;
import info.stasha.testosterone.jersey.TestResponseBuilder;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
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
     * Returns path where request will be send.
     *
     * @return
     */
    private String getPath(String path) {
        Path root = Utils.getAnnotation(target, Path.class);
        String rp = root != null ? root.value() : "";
        return Paths.get(rp, path).toString().replaceAll("\\\\", "/");
    }

    private String getPath() {
        Path p = method.getAnnotation(Path.class);
        return getPath(p == null ? "" : p.value());
    }

    /**
     * {@inheritDoc }
     *
     * @throws Throwable
     */
    @Override
    public void executeTest() throws Throwable {
        LOGGER.info("Executing test {}:{}", method.getDeclaringClass(), method.getName());
        Path root = Utils.getAnnotation(target, Path.class);
        Method resourceMethod = target.getTestConfig().getSetup().getTestInExecution().getMainThreadTest().getClass().getMethod(method.getName(), method.getParameterTypes());
        Path path = resourceMethod.getAnnotation(Path.class);

        String uri = getPath(path.value());

        
        // If path is not generic (instrumented paths start with "__generic__")
        // or if method has @Requests or @Request annotation
        // then we invoke __generic__ endpoint. This is needed to initialize test
        // before @Requests or @Request annotation is invoked.
        if (!uri.startsWith("__generic__") || Utils.hasRequestAnnotation(method)) {
            executeRequest(new RequestAnnotation(getPath("__generic__")), 1);
        } else {
            target.getTestConfig().getSetup().getTestInExecution().setIsRequest(false);
            executeRequest(new RequestAnnotation(uri), 1);
        }
    }

    /**
     * {@inheritDoc }
     *
     * @throws Throwable
     */
    @Override
    public void executeRequests() throws Throwable {
        String path = getPath();

        LOGGER.info("Running: {}:{} at path {}", target.getClass().getName(), method.getName(), path);

        Request requestAnnotation = method.getAnnotation(Request.class);
        Requests requestsAnnotation = method.getAnnotation(Requests.class);

        if (requestAnnotation != null && requestsAnnotation != null) {
            throw new IllegalStateException("@Requests and @Request annotations can't be used on same method: "
                    + method.getName());
        }

        int requestsRepeat = (requestsAnnotation == null || requestsAnnotation.repeat() == 0) ? 1 : requestsAnnotation.repeat();

        int index = 0;
        for (int i = 0; i < requestsRepeat; ++i) {

            Request[] reqs;

            if (requestsAnnotation != null) {
                reqs = requestsAnnotation.requests();
                if (reqs.length == 0) {
                    throw new IllegalStateException("@Requests annotation may not be empty.");
                }
            } else if (requestAnnotation != null) {
                if (requestAnnotation.url().isEmpty()) {
                    RequestAnnotation ra = new RequestAnnotation(requestAnnotation);
                    ra.setUrl(path);
                    requestAnnotation = ra;
                }
                reqs = new Request[]{requestAnnotation};
            } else {
                reqs = new Request[]{new RequestAnnotation(path)};
            }

            for (Request r : reqs) {
                if (Arrays.stream(r.excludeFromRepeat()).anyMatch(((Integer) (i + 1))::equals)) {
                    continue;
                }

                executeRequest(new RequestAnnotation(r), ++index);
            }
        }
    }

    /**
     * Sends http request based on passed Request param.
     *
     * @param request
     * @param index
     */
    protected void executeRequest(RequestAnnotation request, int index) {
        LOGGER.info(request.toString());

        GET get = method.getAnnotation(GET.class);
        POST post = method.getAnnotation(POST.class);
        PUT put = method.getAnnotation(PUT.class);
        DELETE delete = method.getAnnotation(DELETE.class);
        HEAD head = method.getAnnotation(HEAD.class);
        OPTIONS options = method.getAnnotation(OPTIONS.class);

        String requestMethod = HttpMethod.GET;

        if (request.method() != null) {
            requestMethod = request.method();
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

        int requestRepeat = request.repeat() == 0 ? 1 : request.repeat();
        String normalizedUrl = request.url();
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

            if (!request.entity().isEmpty()) {
                try {
                    Field e = target.getClass().getField(request.entity());
                    e.setAccessible(true);
                    Object obj = e.get(target);
                    if (obj instanceof InputStream) {
                        entity = Entity.json(new BufferedReader(new InputStreamReader((InputStream) obj))
                                .lines().collect(Collectors.joining("\n")));
                    } else if (obj instanceof Entity) {
                        entity = (Entity) obj;
                    } else if (obj != null) {
                        entity = (Entity) Entity.json(String.valueOf(obj));
                    }
                } catch (NoSuchFieldException fex) {
                    try {
                        Method en = target.getClass().getMethod(request.entity());
                        entity = (Entity) en.invoke(target, new Object[]{});
                    } catch (NoSuchMethodException mex) {
                        // do nothing
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        java.util.logging.Logger.getLogger(TestExecutorImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } catch (IllegalArgumentException | IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(TestExecutorImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            Invocation.Builder builder = webTarget.request();

            for (String headerParam : request.headerParams()) {
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

            if (!request.url().contains("__generic__")) {
                LOGGER.info(resp.toString());

                // asserting response status if it was set on request
                int status = resp.getStatus();
                String s = Arrays.toString(request.expectedStatus()).replace(",", " or ");
                List<Integer> expectedStatus = Arrays.stream(request.expectedStatus()).boxed().collect(Collectors.toList());
                if (request.expectedStatus().length > 0 && !expectedStatus.contains(status)) {
                    target.getTestConfig().getExceptions()
                            .add(new AssertionError("Response status code should be " + s + ". Expecting " + s + " but was [" + status + "]"));
                }

                int between[] = request.expectedStatusBetween();
                if ((request.expectedStatus().length == 0 && between.length > 0) && (status < between[0] || status > between[1])) {
                    target.getTestConfig().getExceptions()
                            .add(new AssertionError("Response status code should be between [" + between[0] + " and " + between[1]
                                    + "] but was [" + status + "]"));
                }

                List<Object> params = new ArrayList<>();

                for (Class<?> param : method.getParameterTypes()) {
                    if (param.equals(Response.class)) {
                        params.add(resp);
                    } else if (param.equals(Request.class)) {
                        params.add(request);
                    } else if (param.equals(WebTarget.class)) {
                        params.add(webTarget);
                    } else if (param.equals(Integer.class)) {
                        params.add(index++);
                    } else if (param.equals(TestResponseBuilder.TestResponse.class)) {
                        params.add(new TestResponseBuilder()
                                .setIndex(index++).
                                setRequest(request).
                                setResponse(resp).
                                setWebTarget(webTarget).build());
                    }
                }

                if (params.size() > 0) {
                    try {
                        Utils.invokeOriginalMethod(method, target, params.toArray());
                    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException ex) {
                        LOGGER.error("Failed to invoke original test method", ex.getCause());
                        throw new RuntimeException(ex.getCause());
                    }
                }
            }

        }
    }
}
