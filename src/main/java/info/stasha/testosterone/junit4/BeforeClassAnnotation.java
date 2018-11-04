/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info.stasha.testosterone.junit4;

import java.lang.annotation.Annotation;
import org.junit.BeforeClass;

/**
 *
 * @author stasha
 */
public class BeforeClassAnnotation implements BeforeClass {

	@Override
	public Class<? extends Annotation> annotationType() {
		return BeforeClass.class;
	}

}
