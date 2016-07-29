package org.buldakov.spark.bindings.binders;

import org.buldakov.spark.bindings.annotations.HeaderParam;
import org.buldakov.spark.bindings.parameters.MethodParameter;
import spark.Request;

public class HeaderParameterBinder implements ParameterBinder {

    @Override
    public boolean canHandle(MethodParameter<?> parameter) {
        return parameter.hasAnnotation(HeaderParam.class);
    }

    @Override
    public String bind(MethodParameter<?> parameter, Request request) {
        HeaderParam annotation = parameter.getAnnotation(HeaderParam.class);
        return request.headers(annotation.value());
    }
}
