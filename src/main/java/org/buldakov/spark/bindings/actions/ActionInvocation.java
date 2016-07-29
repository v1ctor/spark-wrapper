package org.buldakov.spark.bindings.actions;

import org.buldakov.spark.bindings.parameters.ParameterBinding;
import org.buldakov.spark.bindings.parameters.MethodParameter;
import spark.Route;

import java.lang.reflect.Method;
import java.util.List;

public class ActionInvocation {

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
            return method.invoke(obj, args);
        };
    }

}
