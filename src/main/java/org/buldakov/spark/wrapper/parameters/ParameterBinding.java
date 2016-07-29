package org.buldakov.spark.wrapper.parameters;

import org.buldakov.spark.wrapper.binders.ParameterBinder;
import org.buldakov.spark.wrapper.converters.TypeConverter;

public class ParameterBinding {

    private final MethodParameter<?> parameter;
    private final ParameterBinder binder;
    private final TypeConverter<?> converter;

    public ParameterBinding(MethodParameter<?> parameter, ParameterBinder binder, TypeConverter<?> converter) {
        this.parameter = parameter;
        this.binder = binder;
        this.converter = converter;
    }

    public MethodParameter<?> getParameter() {
        return parameter;
    }

    public ParameterBinder getBinder() {
        return binder;
    }

    public TypeConverter<?> getConverter() {
        return converter;
    }

}
