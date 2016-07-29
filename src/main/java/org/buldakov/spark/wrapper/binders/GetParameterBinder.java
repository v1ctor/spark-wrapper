package org.buldakov.spark.wrapper.binders;

import org.buldakov.spark.wrapper.annotations.GetParam;
import org.buldakov.spark.wrapper.parameters.MethodParameter;
import spark.Request;

public class GetParameterBinder implements ParameterBinder {

    @Override
    public boolean canHandle(MethodParameter<?> parameter) {
        return parameter.hasAnnotation(GetParam.class);
    }

    @Override
    public String bind(MethodParameter<?> parameter, Request request) {
        GetParam annotation = parameter.getAnnotation(GetParam.class);
        return request.queryParams(annotation.value());
    }
}
