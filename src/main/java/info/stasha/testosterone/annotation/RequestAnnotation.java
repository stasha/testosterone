package info.stasha.testosterone.annotation;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import javax.ws.rs.HttpMethod;

/**
 * Implementation of Request annotation
 *
 * @author stasha
 */
public class RequestAnnotation implements Request {

    private int repeat = 1;
    private int[] excludeFromRepeat = {};
    private String url;
    private String[] headerParams = {};
    private String method = HttpMethod.GET;
    private int[] expectedStatus = {};
    private String entity = "";

    /**
     * Creates new RequestAnnotation instance.
     */
    public RequestAnnotation() {
    }

    public RequestAnnotation(Request request) {
        this.entity = request.entity();
        this.excludeFromRepeat = request.excludeFromRepeat();
        this.expectedStatus = request.expectedStatus();
        this.headerParams = request.headerParams();
        this.method = request.method();
        this.repeat = request.repeat();
        this.url = request.url();
    }

    /**
     * Creates RequestAnnotation with default url.
     *
     * @param url
     */
    public RequestAnnotation(String url) {
        this.url = url;
    }

    /**
     * Sets request repeat number.
     *
     * @param repeat
     */
    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    /**
     * Sets exclude from repeat number/s.
     *
     * @param excludeFromRepeat
     */
    public void setExcludeFromRepeat(int[] excludeFromRepeat) {
        this.excludeFromRepeat = excludeFromRepeat;
    }

    /**
     * Sets url for request.
     *
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Set header params.
     *
     * @param headerParams
     */
    public void setHeaderParams(String[] headerParams) {
        this.headerParams = headerParams;
    }

    /**
     * Sets http method.
     *
     * @param method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Sets expected status code.
     *
     * @param expectedStatus
     */
    public void setExpectedStatus(int[] expectedStatus) {
        this.expectedStatus = expectedStatus;
    }

    /**
     * Sets field or method name where to look for entity.
     *
     * @param entity
     */
    public void setEntity(String entity) {
        this.entity = entity;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public Class<? extends Annotation> annotationType() {
        return Request.class;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public int repeat() {
        return this.repeat;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public int[] excludeFromRepeat() {
        return this.excludeFromRepeat;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String url() {
        return this.url;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String[] headerParams() {
        return this.headerParams;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public String method() {
        return this.method;
    }

    /**
     * {@inheritDoc }
     *
     * @return
     */
    @Override
    public int[] expectedStatus() {
        return this.expectedStatus;
    }

    @Override
    public String entity() {
        return this.entity;
    }

    @Override
    public String toString() {
        return "RequestAnnotation{" + "repeat=" + repeat + ", excludeFromRepeat=" + Arrays.toString(excludeFromRepeat) + ", url=" + url + ", headerParams=" + Arrays.toString(headerParams) + ", method=" + method + ", expectedStatus=" + Arrays.toString(expectedStatus) + ", entity=" + entity + '}';
    }

}
