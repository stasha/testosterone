package info.stasha.testosterone.jersey;

import info.stasha.testosterone.annotation.Request;
import javax.ws.rs.client.WebTarget;

/**
 *
 * @author stasha
 */
public class TestResponseBuilder {

    private TestResponse resp = new TestResponse();

    public TestResponse build() {
        TestResponse r = resp;
        resp = null;
        return r;
    }

    public TestResponseBuilder setIndex(int index) {
        resp.repeatIndex = index;
        return this;
    }

    public TestResponseBuilder setResponse(javax.ws.rs.core.Response response) {
        resp.response = response;
        return this;
    }

    public TestResponseBuilder setRequest(Request request) {
        resp.request = request;
        return this;
    }

    public TestResponseBuilder setWebTarget(WebTarget webTarget) {
        resp.webTarget = webTarget;
        return this;
    }

    public class TestResponse {

        private javax.ws.rs.core.Response response;
        private Request request;
        private int repeatIndex;
        private WebTarget webTarget;

        public javax.ws.rs.core.Response getResponse() {
            return response;
        }

        public Request getRequest() {
            return request;
        }

        public int getRepeatIndex() {
            return repeatIndex;
        }

        public WebTarget getWebTarget() {
            return webTarget;
        }

    }
}
