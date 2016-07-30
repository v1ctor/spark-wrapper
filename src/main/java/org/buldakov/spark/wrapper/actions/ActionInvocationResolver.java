package org.buldakov.spark.wrapper.actions;

import org.buldakov.spark.wrapper.annotations.Action;
import org.buldakov.spark.wrapper.annotations.ActionController;
import org.buldakov.spark.wrapper.binders.BodyParameterBinder;
import org.buldakov.spark.wrapper.binders.GetParameterBinder;
import org.buldakov.spark.wrapper.binders.HeaderParameterBinder;
import org.buldakov.spark.wrapper.binders.ParameterBinder;
import org.buldakov.spark.wrapper.binders.PathParameterBinder;
import org.buldakov.spark.wrapper.converters.PrimitiveTypeConverter;
import org.buldakov.spark.wrapper.converters.TypeConverter;
import org.buldakov.spark.wrapper.parameters.MethodParameter;
import org.buldakov.spark.wrapper.parameters.ParameterBinding;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger LOGGER = LoggerFactory.getLogger(ActionInvocationResolver.class);

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
                LOGGER.error(obj.getClass().getName() + "." + method.getName() + ": Can't resolve constructor for type "
                        + type.getName());
                continue;
            }

            Optional<ParameterBinder> parameterBinder = binders.stream()
                    .filter(binder -> binder.canHandle(methodParameter)).findFirst();

            if (!parameterBinder.isPresent()) {
                LOGGER.error(obj.getClass().getName() + "." + method.getName() + ": Can't resolve constructor for type "
                        + type.getName());
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
