package org.buldakov.spark.bindings.binders;

import org.buldakov.spark.bindings.annotations.Body;
import org.buldakov.spark.bindings.parameters.MethodParameter;
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
