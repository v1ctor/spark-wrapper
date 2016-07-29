package org.buldakov.spark.wrapper.binders;

import org.buldakov.spark.wrapper.annotations.Body;
import org.buldakov.spark.wrapper.parameters.MethodParameter;
import spark.Request;

public class BodyParameterBinder implements ParameterBinder {

    @Override
    public boolean canHandle(MethodParameter<?> parameter) {
        return parameter.hasAnnotation(Body.class);
    }

    @Override
    public String bind(MethodParameter<?> parameter, Request request) {
        return request.body();
    }
}
