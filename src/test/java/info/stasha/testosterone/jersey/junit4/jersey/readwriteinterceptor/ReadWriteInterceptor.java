/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.jersey.junit4.jersey.readwriteinterceptor;

import java.io.IOException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.ReaderInterceptor;
import javax.ws.rs.ext.ReaderInterceptorContext;
import javax.ws.rs.ext.WriterInterceptor;
import javax.ws.rs.ext.WriterInterceptorContext;

/**
 *
 * @author stasha
 */
public class ReadWriteInterceptor implements ReaderInterceptor, WriterInterceptor {

	public static final String READ_FROM_TEXT = "Around read from interceptor";
	public static final String WRITE_TO_TEXT = "Around write to interceptor";

	@Override
	public Object aroundReadFrom(ReaderInterceptorContext ric) throws IOException, WebApplicationException {
		return READ_FROM_TEXT;
	}

	@Override
	public void aroundWriteTo(WriterInterceptorContext wic) throws IOException, WebApplicationException {
		wic.setEntity(WRITE_TO_TEXT);
		wic.proceed();
	}

}
