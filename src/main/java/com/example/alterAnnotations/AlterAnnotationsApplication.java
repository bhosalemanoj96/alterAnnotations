package com.example.alterAnnotations;

/*import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;*/

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

//@SpringBootApplication
public class AlterAnnotationsApplication {

	/*public static void main(String[] args) {
		SpringApplication.run(AlterAnnotationsApplication.class, args);
	}*/

	public static void main(String[] args) throws NoSuchFieldException {
		Greeter greet = Greetings.class.getAnnotation(Greeter.class);
		System.err.println("Hello there [" + greet.greet() + "]");
		DynamicGreetings altered = new DynamicGreetings("KungFu Panda");
		alterClassAnnotation(Greetings.class, Greeter.class, altered);
		greet = Greetings.class.getAnnotation(Greeter.class);
		System.err.println("After alteration...Hello there [" + greet.greet() + "]");

		Field field = Greetings.class.getDeclaredField("field1");
		Greeter greet1 = field.getAnnotation(Greeter.class);
		System.err.println("Hello there [" + greet1.greet() + "]");
		DynamicGreetings altered1 = new DynamicGreetings("KungFu Panda");
		alterFieldAnnotation(field, Greeter.class, altered1);
		greet1 = field.getAnnotation(Greeter.class);
		System.err.println("After alteration...Hello there [" + greet1.greet() + "]");
	}


	private static void alterClassAnnotation(Class clazzToLookFor, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) {
		String ANNOTATIONS = "annotations";
		String ANNOTATION_DATA = "annotationData";
		try {
			//In JDK8 Class has a private method called annotationData().
			//We first need to invoke it to obtain a reference to AnnotationData class which is a private class
			Method method = Class.class.getDeclaredMethod(ANNOTATION_DATA, null);
			method.setAccessible(true);
			//Since AnnotationData is a private class we cannot create a direct reference to it. We will have to
			//manage with just Object
			Object annotationData = method.invoke(clazzToLookFor);
			//We now look for the map called "annotations" within AnnotationData object.
			Field annotations = annotationData.getClass().getDeclaredField(ANNOTATIONS);
			annotations.setAccessible(true);
			Map<Class<? extends Annotation>, Annotation> map =
					(Map<Class<? extends Annotation>, Annotation>) annotations.get(annotationData);
			map.put(annotationToAlter, annotationValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void alterFieldAnnotation(Field field, Class<? extends Annotation> annotationToAlter, Annotation annotationValue) throws NoSuchFieldException {
		String ANNOTATIONS = "annotations";
		String ANNOTATION_DATA = "annotationData";
		try {
			Annotation annotation = field.getAnnotation(annotationToAlter);

			// Use reflection to get the annotation's InvocationHandler
			InvocationHandler handler = Proxy.getInvocationHandler(annotation);

			// Get the annotation's memberValues field
			Field memberValuesField = handler.getClass().getDeclaredField("memberValues");
			memberValuesField.setAccessible(true);

			// Get the map of member values from the handler
			@SuppressWarnings("unchecked")
			Map<String, Object> memberValues = (Map<String, Object>) memberValuesField.get(handler);

			// Update the member values with the new values
			/*for (Map.Entry<String, Object> entry : newValues.entrySet()) {
				memberValues.put(entry.getKey(), entry.getValue());
			}*/
			memberValues.put("greet","hello");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
