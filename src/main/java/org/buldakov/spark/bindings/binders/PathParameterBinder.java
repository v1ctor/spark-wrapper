package org.buldakov.spark.bindings.binders;

import org.buldakov.spark.bindings.annotations.PathParam;
import org.buldakov.spark.bindings.parameters.MethodParameter;
import spark.Request;

public class PathParameterBinder implements ParameterBinder {

    @Override
    public boolean canHandle(MethodParameter<?> parameter) {
        return parameter.hasAnnotation(PathParam.class);
    }

    @Override
    public String bind(MethodParameter<?> parameter, Request request) {
        PathParam annotation = parameter.getAnnotation(PathParam.class);
        return request.params(annotation.value());
    }
}
