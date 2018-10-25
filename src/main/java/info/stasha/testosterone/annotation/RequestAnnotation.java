package info.stasha.testosterone.annotation;

import java.lang.annotation.Annotation;
import javax.ws.rs.HttpMethod;

/**
 * Implementation of Request annotation
 *
 * @author stasha
 */
public class RequestAnnotation implements Request {

	private int repeat = 1;
	private int[] excludeFromRepeat = new int[]{};
	private String url;
	private String[] headerParams = new String[]{};
	private String method = HttpMethod.GET;

	public RequestAnnotation() {
	}

	public RequestAnnotation(String url) {
		this.url = url;
	}

	public void setRepeat(int repeat) {
		this.repeat = repeat;
	}

	public void setExcludeFromRepeat(int[] excludeFromRepeat) {
		this.excludeFromRepeat = excludeFromRepeat;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setHeaderParams(String[] headerParams) {
		this.headerParams = headerParams;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	@Override
	public Class<? extends Annotation> annotationType() {
		return Request.class;
	}

	@Override
	public int repeat() {
		return this.repeat;
	}

	@Override
	public int[] excludeFromRepeat() {
		return this.excludeFromRepeat;
	}

	@Override
	public String url() {
		return this.url;
	}

	@Override
	public String[] headerParams() {
		return this.headerParams;
	}

	@Override
	public String method() {
		return this.method;
	}

}
