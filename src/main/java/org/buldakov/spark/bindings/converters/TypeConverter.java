package org.buldakov.spark.bindings.converters;

import org.buldakov.spark.bindings.parameters.MethodParameter;

public interface TypeConverter<T> {

    T convert(MethodParameter<?> parameter, String value);

    boolean canHandle(MethodParameter<?> parameter);
}
