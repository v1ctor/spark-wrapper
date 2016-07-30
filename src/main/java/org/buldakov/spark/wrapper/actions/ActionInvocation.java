package org.buldakov.spark.wrapper.actions;

import org.buldakov.spark.wrapper.parameters.ParameterBinding;
import org.buldakov.spark.wrapper.parameters.MethodParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Route;

import java.lang.reflect.Method;
import java.util.List;

public class ActionInvocation {

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionInvocation.class);

    private final Object obj;
    private final Method method;
    private final List<ParameterBinding> bindings;

    public ActionInvocation(Object obj, Method method, List<ParameterBinding> bindings) {
        this.obj = obj;
        this.method = method;
        this.bindings = bindings;
    }

    public Route getRoute() {
        return (request, response) -> {
            Object[] args = new Object[method.getParameterCount()];
            bindings.forEach(binding -> {
                MethodParameter<?> parameter = binding.getParameter();
                String value = binding.getBinder().bind(parameter, request);
                Object convertedValue = binding.getConverter().convert(parameter, value);
                args[parameter.getIndex()] = convertedValue;
            });
            try {
                return method.invoke(obj, args);
            } catch (Exception e) {
                LOGGER.error("Error binding methd params ", e);
                throw e;
            }
        };
    }

}
