package org.buldakov.spark.bindings.parameters;

import java.lang.annotation.Annotation;
import java.util.NoSuchElementException;

public class MethodParameter<T> {

    private final Annotation[] annotations;
    private final Class<T> type;
    private final int index;

    public MethodParameter(Annotation[] annotations, Class<T> type, int index) {
        this.annotations = annotations;
        this.type = type;
        this.index = index;
    }

    public boolean hasAnnotation(Class<? extends Annotation> type) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(type)) {
                return true;
            }
        }
        return false;
    }

    public <V extends Annotation> V getAnnotation(Class<V> type) {
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().equals(type)) {
                return type.cast(annotation);
            }
        }
        //TODO ?
        throw new NoSuchElementException();
    }

    public Class<T> getType() {
        return type;
    }

    public int getIndex() {
        return index;
    }
}
