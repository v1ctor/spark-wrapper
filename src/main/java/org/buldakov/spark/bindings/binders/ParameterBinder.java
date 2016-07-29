package org.buldakov.spark.bindings.binders;

import org.buldakov.spark.bindings.parameters.MethodParameter;
import spark.Request;

public interface ParameterBinder {

    boolean canHandle(MethodParameter<?> parameter);

    String bind(MethodParameter<?> parameter, Request request);

}
