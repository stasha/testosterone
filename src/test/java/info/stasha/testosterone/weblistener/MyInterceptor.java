/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.weblistener;

import java.lang.reflect.Constructor;
import net.bytebuddy.asm.Advice.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

/**
 *
 * @author stasha
 */
public class MyInterceptor {

	@RuntimeType
	public static void intercept(@Origin Constructor cons) {
		System.out.println("Intercepted: ");
	}
}
