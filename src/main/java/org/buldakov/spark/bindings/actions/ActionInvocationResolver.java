package org.buldakov.spark.bindings.actions;

import org.buldakov.spark.bindings.parameters.ParameterBinding;
import org.buldakov.spark.bindings.annotations.Action;
import org.buldakov.spark.bindings.annotations.ActionController;
import org.buldakov.spark.bindings.binders.BodyParameterBinder;
import org.buldakov.spark.bindings.binders.GetParameterBinder;
import org.buldakov.spark.bindings.binders.HeaderParameterBinder;
import org.buldakov.spark.bindings.binders.ParameterBinder;
import org.buldakov.spark.bindings.binders.PathParameterBinder;
import org.buldakov.spark.bindings.converters.PrimitiveTypeConverter;
import org.buldakov.spark.bindings.converters.TypeConverter;
import org.buldakov.spark.bindings.parameters.MethodParameter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ActionInvocationResolver {

    private final List<TypeConverter<?>> converters;
    private final List<ParameterBinder> binders;

    public ActionInvocationResolver(List<TypeConverter<?>> converters, List<ParameterBinder> binders) {
        this.converters = converters;
        this.binders = binders;
    }

    public List<ActionDescriptor> resolve(Object obj) {
        ActionController annotation = obj.getClass().getAnnotation(ActionController.class);
        if (annotation == null) {
            return Collections.emptyList();
        }
        List<ActionDescriptor> descriptors = new ArrayList<>();
        for (Method method : obj.getClass().getMethods()) {
            Action action = method.getAnnotation(Action.class);
            if (action == null) {
                continue;
            }
            ActionInvocation invocation = resolve(obj, method);
            descriptors.add(new ActionDescriptor(action.method(), action.value(), invocation));
        }
        return descriptors;
    }

    private ActionInvocation resolve(Object obj, Method method) {
        List<ParameterBinding> bindings = new ArrayList<>(method.getParameterCount());
        for (int index = 0; index < method.getParameterCount(); index++) {
            Parameter parameter = method.getParameters()[index];
            Annotation[] annotations = parameter.getAnnotations();
            Class<?> type = parameter.getType();

            MethodParameter<?> methodParameter = new MethodParameter<>(annotations, type, index);
            Optional<TypeConverter<?>> typeConverter = converters.stream()
                    .filter(converter -> converter.canHandle(methodParameter)).findFirst();
            if (!typeConverter.isPresent()) {
                //TODO unknown param
                continue;
            }

            Optional<ParameterBinder> parameterBinder = binders.stream()
                    .filter(binder -> binder.canHandle(methodParameter)).findFirst();

            if (!parameterBinder.isPresent()) {
                //TODO unknown param
                continue;
            }

            bindings.add(new ParameterBinding(methodParameter, parameterBinder.get(), typeConverter.get()));
        }
        return new ActionInvocation(obj, method, bindings);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Builder() {}

        private final Set<TypeConverter<?>> converters = new HashSet<>();
        private final Set<ParameterBinder> binders = new HashSet<>();

        public Builder withBinder(ParameterBinder binder) {
            binders.add(binder);
            return this;
        }

        public Builder withConverter(TypeConverter<?> converter) {
            converters.add(converter);
            return this;
        }

        public Builder withDefaultConverters() {
            converters.add(new PrimitiveTypeConverter());
            return this;
        }

        public Builder withDefaultBinders() {
            binders.add(new BodyParameterBinder());
            binders.add(new PathParameterBinder());
            binders.add(new HeaderParameterBinder());
            binders.add(new GetParameterBinder());
            return this;
        }

        public ActionInvocationResolver build() {
            return new ActionInvocationResolver(new ArrayList<>(converters), new ArrayList<>(binders));
        }
    }
}
