package com.example.alterAnnotations;

import java.lang.annotation.Annotation;

public class DynamicGreetings implements Greeter {

    private String greet;

    public DynamicGreetings(String greet) {
        this.greet = greet;
    }

    @Override
    public String greet() {
        return greet;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return DynamicGreetings.class;
    }
}
