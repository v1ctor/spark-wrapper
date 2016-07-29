package org.buldakov.spark.wrapper.binders;

import org.buldakov.spark.wrapper.parameters.MethodParameter;
import spark.Request;

public interface ParameterBinder {

    boolean canHandle(MethodParameter<?> parameter);

    String bind(MethodParameter<?> parameter, Request request);

}
