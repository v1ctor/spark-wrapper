package org.buldakov.spark.wrapper.converters;

import org.buldakov.spark.wrapper.parameters.MethodParameter;

public interface TypeConverter<T> {

    T convert(MethodParameter<?> parameter, String value);

    boolean canHandle(MethodParameter<?> parameter);
}
