package org.buldakov.spark.wrapper.converters;

import org.buldakov.spark.wrapper.parameters.MethodParameter;

public class PrimitiveTypeConverter implements TypeConverter<Object> {

    @Override
    public Object convert(MethodParameter<?> parameter, String value) {
        if (parameter.getType().isAssignableFrom(Long.class)) {
            return Long.parseLong(value);
        } else if (parameter.getType().isAssignableFrom(Integer.class)) {
            return Integer.parseInt(value);
        } else if (parameter.getType().isAssignableFrom(Short.class)) {
            return Short.parseShort(value);
        } else if (parameter.getType().isAssignableFrom(Byte.class)) {
            return Byte.parseByte(value);
        } else if (parameter.getType().isAssignableFrom(Float.class)) {
            return Float.parseFloat(value);
        } else if (parameter.getType().isAssignableFrom(Double.class)) {
            return Double.parseDouble(value);
        } else if (parameter.getType().isAssignableFrom(Boolean.class)) {
            return Boolean.parseBoolean(value);
        } else if (parameter.getType().isAssignableFrom(String.class)) {
            return value;
        } else if (parameter.getType().isAssignableFrom(Character.class)) {
            if (value.length() != 1) {
                //TODO
                throw new IllegalArgumentException("Not a char");
            }
            return value.charAt(0);
        }
        //TODO
        throw new IllegalArgumentException("Unknown type");
    }

    @Override
    public boolean canHandle(MethodParameter<?> parameter) {
        return parameter.getType().isAssignableFrom(Long.class)
                || parameter.getType().isAssignableFrom(Integer.class)
                || parameter.getType().isAssignableFrom(Short.class)
                || parameter.getType().isAssignableFrom(Byte.class)
                || parameter.getType().isAssignableFrom(Float.class)
                || parameter.getType().isAssignableFrom(Double.class)
                || parameter.getType().isAssignableFrom(Character.class)
                || parameter.getType().isAssignableFrom(Boolean.class)
                || parameter.getType().isAssignableFrom(String.class);
    }

}
